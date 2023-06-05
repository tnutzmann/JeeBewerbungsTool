package de.jinba.server.util;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ModelConfigurer {
    private final Model model;
    public ModelConfigurer(Model model) {
        this.model = model;
    }
    public static ModelConfigurer of(Model model) {
        return new ModelConfigurer(model);
    }
    public ModelConfigurer with(String attributeName, Object attributeValue) {
        if(!model.asMap().containsKey(attributeName))
            if(model instanceof RedirectAttributes)
                ((RedirectAttributes) model).addFlashAttribute(attributeName, attributeValue);
            else
                model.addAttribute(attributeName, attributeValue);
        return this;
    }
}
