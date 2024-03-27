package uel.br.administrador;

//import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestauranteController {

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    ItemCardapioRepository cardapioRepository;

    @GetMapping("/novo-restaurante")
    public String mostrarFormNovoRestaurante(Restaurante restaurante){
        return "novo-restaurante";
    }

    @GetMapping(value={"/restaurantes", "/"})
    public String mostrarListaRestaurante(Model model) {
        model.addAttribute("restaurantes", restauranteRepository.findAll());
        return "restaurantes";
    }

    @PostMapping("/adicionar-restaurante")
    public String adicionarRestaurante(@Valid Restaurante restaurante, BindingResult result) {
        if(result.hasErrors())
            return "/novo-restaurante";

        restauranteRepository.save(restaurante);
        return "redirect:/restaurantes";
    }

    @GetMapping("/editar/restaurante/{id}")
    public String mostrarFormAtualizar(@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));

        model.addAttribute("restaurante", restaurante);
        return "atualizar-restaurante";
    }

    @PostMapping("/atualizar/restaurante/{id}")
    public String atualizarRestaurante(@PathVariable("id") int id, @Valid Restaurante restaurante,
                                   BindingResult result, Model model, HttpServletRequest request) {
        if(result.hasErrors()) {
            restaurante.setId(id);
            model.addAttribute("restaurante", restaurante);
            return "atualizar-restaurante";
        }
        restauranteRepository.save(restaurante);

        return "redirect:/restaurantes";
    }

    @GetMapping("/remover/restaurante/{id}")
    public String removerRestaurante(@PathVariable("id") int id, HttpServletRequest request) {
        Restaurante restaurante = restauranteRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));
        restauranteRepository.delete(restaurante);

        return "redirect:/restaurantes";
    }

    @GetMapping("/cardapio/{id}")
    public String mostrarCardapio(@PathVariable("id") int id, Model model) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("O id do restaurante é inválido:" + id));

        model.addAttribute("cardapio", cardapioRepository.findByRestauranteId(id));
        model.addAttribute("idRestaurante", id);
        model.addAttribute("nomeRestaurante", restaurante.getNome());

        return "cardapio";
    }

}
