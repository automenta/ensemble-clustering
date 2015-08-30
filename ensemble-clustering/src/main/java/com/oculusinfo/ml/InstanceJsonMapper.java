/**
 * Copyright (c) 2013 Oculus Info Inc.
 * http://www.oculusinfo.com/
 *
 * Released under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oculusinfo.ml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;



/***
 * Serializer for Instance object to/from JSON
 * 
 * @author slangevin
 *
 */
public class InstanceJsonMapper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonFactory factory = new JsonFactory();

    public static Instance fromJson(String jsonAsString)
    	throws IOException {
    
    	mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    	Instance inst = mapper.readValue(jsonAsString, Instance.class);
    	
    	return inst;
    }

    public static String toJson(Instance inst, boolean prettyPrint) 
    	throws IOException {
        
    	StringWriter writer = new StringWriter();
        JsonGenerator generator = factory.createGenerator(writer);
        
        if (prettyPrint) {
            generator.useDefaultPrettyPrinter();
        }
        
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.writeValue(generator, inst);
        return writer.toString();
    }
}