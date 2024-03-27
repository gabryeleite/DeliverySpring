package uel.br.consumidor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.ArrayList;

@Controller
public class ItemCardapioController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    ItemCardapioRepository cardapioRepository;

    private static final String SESSION_PEDIDO = "sessionPedido";
    private static final String SESSION_TOTAL = "sessionTotal";

    @GetMapping("/cardapio/{id}")
    public String mostrarCardapio(@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));

        model.addAttribute("cardapio", cardapioRepository.findByRestauranteId(id));
        model.addAttribute("idRestaurante", id);
        model.addAttribute("nomeRestaurante", restaurante.getNome());

        return "cardapio";
    }

    @GetMapping("/pedido")
    public String mostrarPedido(Model model, HttpServletRequest request){
        List<ItemCardapio> pedido = (List<ItemCardapio>)request.getSession().getAttribute(SESSION_PEDIDO);
        Double valorTotal = (Double)request.getSession().getAttribute(SESSION_TOTAL);

        if(valorTotal == null) valorTotal = 0.0;

        model.addAttribute("sessionPedido",
                !CollectionUtils.isEmpty(pedido) ? pedido : new ArrayList<>());
        model.addAttribute("valorTotal", valorTotal);

        return "pedido";
    }

    @GetMapping("/adicionarPedido/{id}")
    public String adicionarItemPedido (@PathVariable("id") int id, HttpServletRequest request) {
        ItemCardapio itemCardapio = cardapioRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("O id do item é inválido: " + id));
        List<Pedido> pedido = (List<Pedido>)request.getSession().getAttribute(SESSION_PEDIDO);

        // se o pedido esta vazio, cria um novo pedido
        if(pedido == null) pedido = new ArrayList<Pedido>();

        boolean existeItemPedido = false;
        for(Pedido itemPedido : pedido) {
            if(itemPedido.getId() == itemCardapio.getId()) {
                existeItemPedido = true; // se o item ja esta no pedido, aumenta quantidade
                itemPedido.setQuantidade(itemPedido.getQuantidade() + 1);
                break;
            }
        }

        if(!existeItemPedido) { // se o item ainda nao estiver no carrinho, adiciona
            Pedido itemPedido = new Pedido(itemCardapio.getId(), itemCardapio.getNome(),
                    itemCardapio.getDescricao(), itemCardapio.getPreco(), itemCardapio.getRestaurante().getNome());
            pedido.add(itemPedido);
        }

        Double valorTotal = 0.0;
        for(Pedido itemPedido : pedido) {
            valorTotal = valorTotal + Double.valueOf(itemPedido.getQuantidade() * itemPedido.getPreco());
            itemPedido.setValorTotal(valorTotal);
        }

        request.getSession().setAttribute(SESSION_PEDIDO, pedido);
        request.getSession().setAttribute(SESSION_TOTAL, valorTotal);
        return "redirect:/pedido";
    }

    @GetMapping("/removerPedido/{id}")
    public String removerItemPedido (@PathVariable("id") int id, HttpServletRequest request) {
        ItemCardapio itemCardapio = cardapioRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("O id do item é inválido: " + id));
        List<Pedido> pedido = (List<Pedido>)request.getSession().getAttribute(SESSION_PEDIDO);

        for(Pedido itemPedido : pedido) {
            if(itemPedido.getId() == itemCardapio.getId()) {
                if(itemPedido.getQuantidade() > 1) // se tiver mais de 1 item, reduz a quantidade
                    itemPedido.setQuantidade(itemPedido.getQuantidade() - 1);
                else // se tiver so 1 item no pedido, remove
                    pedido.remove(itemPedido);
                break;
            }
        }

        Double valorTotal = 0.0;
        for(Pedido itemPedido : pedido) {
            valorTotal = valorTotal + Double.valueOf(itemPedido.getQuantidade() * itemPedido.getPreco());
            itemPedido.setValorTotal(valorTotal);
        }

        request.getSession().setAttribute(SESSION_PEDIDO, pedido);
        request.getSession().setAttribute(SESSION_TOTAL, valorTotal);
        return "redirect:/pedido";
    }

}
