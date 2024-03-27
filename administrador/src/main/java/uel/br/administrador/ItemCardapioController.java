package uel.br.administrador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;
import java.util.List;

@Controller
public class ItemCardapioController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    ItemCardapioRepository cardapioRepository;

    private static final String SESSION_PEDIDO = "sessionPedido";
    private static final String SESSION_TOTAL = "valorTotal";

    @GetMapping("/novo-item-cardapio/{id}")
    public String mostrarFormNovoItemCardapio(@PathVariable("id") int id,
                                              ItemCardapio itemCardapio, Model model){
        model.addAttribute("idRestaurante", id);
        //model.addAttribute("itemCardapio", itemCardapio);
        return "novo-item-cardapio";
    }

    @PostMapping("/adicionar-item-cardapio/{id}")
    public String adicionarItemCardapio(@PathVariable("id") int id,
                                        @Valid ItemCardapio itemCardapio, BindingResult result) {
        if(result.hasErrors())
            return "/novo-item-cardapio";

        Restaurante restaurante = restauranteRepository.findById(id).orElse(null);
        itemCardapio.setRestaurante(restaurante);
        cardapioRepository.save(itemCardapio);

        return "redirect:/cardapio/{id}";
    }

    @GetMapping("/editar/item/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model) {
        ItemCardapio itemCardapio = cardapioRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("O id do item é inválido:" + id));

        model.addAttribute("itemCardapio", itemCardapio);
        model.addAttribute("restaurante", itemCardapio.getRestaurante());
        return "atualizar-item-cardapio";
    }

    @PostMapping("/atualizar/item/{id}")
    public String atualizarItemCardapio(@PathVariable("id") int id, @Valid ItemCardapio itemCardapio,
                                       BindingResult result, Model model, HttpServletRequest request) {
        if(result.hasErrors()) {
            itemCardapio.setId(id);
            model.addAttribute("itemCardapio", itemCardapio);
            return "atualizar-item-cardapio";
        }
        Restaurante restaurante = itemCardapio.getRestaurante();
        cardapioRepository.save(itemCardapio);

        return "redirect:/cardapio/" + restaurante.getId();

    }

    @GetMapping("/remover/item/{id}")
    public String removerItemCardapio(@PathVariable("id") int id, HttpServletRequest request) {
        ItemCardapio itemCardapio = cardapioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do item é inválido:" + id));
        Restaurante restaurante = itemCardapio.getRestaurante();
        cardapioRepository.delete(itemCardapio);

        return "redirect:/cardapio/" + restaurante.getId();
    }

}
