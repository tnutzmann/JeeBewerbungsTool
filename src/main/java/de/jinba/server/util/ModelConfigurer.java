package de.jinba.server.util;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class is used to configure a {@link Model} with attributes. It can also be used to
 * configure a {@link RedirectAttributes} object. It respects the flash attribute mechanism
 * of the {@link RedirectAttributes} object. Furthermore, it does not override existing attributes.
 */
public class ModelConfigurer {
    private final Model model;
    public ModelConfigurer(Model model) {
        this.model = model;
    }

    /**
     * Creates a new instance of {@link ModelConfigurer} with the given {@link Model}.
     * @param model The model to configure.
     * @return A new instance of {@link ModelConfigurer}.
     */
    public static ModelConfigurer of(Model model) {
        return new ModelConfigurer(model);
    }

    /**
     * Adds the given attribute to the model if it does not exist yet. If the model is of type {@link RedirectAttributes},
     * the attribute will be added as a flash attribute.
     * @param attributeName The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @return The current instance of {@link ModelConfigurer}.
     */
    public ModelConfigurer with(String attributeName, Object attributeValue) {
        if(!model.asMap().containsKey(attributeName))
            if(model instanceof RedirectAttributes)
                ((RedirectAttributes) model).addFlashAttribute(attributeName, attributeValue);
            else
                model.addAttribute(attributeName, attributeValue);
        return this;
    }
}
