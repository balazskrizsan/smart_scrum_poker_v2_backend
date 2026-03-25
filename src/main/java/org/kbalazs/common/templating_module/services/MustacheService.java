package org.kbalazs.common.templating_module.services;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;

@Service
public class MustacheService
{
    public String execute(@NonNull String template, @NonNull Object templateData) throws IOException
    {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile(template);
        StringWriter writer = new StringWriter();
        mustache.execute(writer, templateData).flush();

        return writer.toString();
    }
}
