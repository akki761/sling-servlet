/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.akash.boot.osgi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.slf4j.LoggerFactory;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

///**
// * Hello World Servlet registered by path
// *
// * Annotations below are short version of:
// *
// * @Component
// * @Service(Servlet.class)
// * @Properties({
// *     @Property(name="service.description", value="Hello World Path Servlet"),
// *     @Property(name="service.vendor", value="The Apache Software Foundation"),
// *     @Property(name="sling.servlet.paths", value="/hello-world-servlet")
// * })
// */
//@SlingServlet(paths="/hello-world-servlet")
//@Properties({
//    @Property(name="service.description", value="Hello World Path Servlet"),
//    @Property(name="service.vendor", value="The Apache Software Foundation")
//})

@Component(
        service = {Servlet.class},
        property = {
                SLING_SERVLET_PATHS + "=/blog-page"
        }
)

@SuppressWarnings("serial")
public class ByPathServlet extends SlingSafeMethodsServlet {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(ByPathServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        Document document=new Document();
       // Writer w=response.getWriter();

        ServletOutputStream servletOutputStream=response.getOutputStream();



        response.setContentType("application/pdf");
        PdfWriter pdfWriter=null;
        try {
             pdfWriter =PdfWriter.getInstance(document,servletOutputStream);

            document.open();

            Paragraph p = new Paragraph();
            String req=request.getParameter("orderby");

            Resource resource = request.getResourceResolver().getResource("/content/blog/");


            Iterator<Resource> resourceIterator=resource.listChildren();
       //   ArrayList<Resource> resourceArrayList=new ArrayList<>();

            ArrayList<Resource> resourceArrayList=new ArrayList<>();
            while(resourceIterator.hasNext()){
                 resourceArrayList.add(resourceIterator.next());
             }

             if (req.equals("asc")){
             Collections.sort(resourceArrayList, new Comparator<Resource>() {
                 @Override
                 public int compare(Resource resource, Resource t1) {
                     ValueMap valueMap=resource.getValueMap();
                     ValueMap valueMap1=t1.getValueMap();
                     String s = valueMap.get("jcr:created","Default Resource");
                     String s1=valueMap1.get("jcr:created","Default Resource2");
                     if(s.compareTo(s1)>0)
                         return 1;
                     else if(s.compareTo(s1)<0)
                         return -1;
                     else return 0;
                 }
             });
    }
        if(req.equals("desc")){
            Collections.sort(resourceArrayList, new Comparator<Resource>() {
                @Override
                public int compare(Resource resource, Resource t1) {
                    ValueMap valueMap=resource.getValueMap();
                    ValueMap valueMap1=t1.getValueMap();
                    String s = valueMap.get("jcr:created","Default Resource");
                    String s1=valueMap1.get("jcr:created","Default Resource2");
                    if(s.compareTo(s1)>0)
                        return -1;
                    else if(s.compareTo(s1)<0)
                        return 1;
                    else return 0;
                }
            });
        }

             for(Resource r: resourceArrayList) {
              document.add(new Paragraph(r.getName()+"  "+r.getValueMap().get("jcr:created","default")+"\n"));
             }



//         ValueMap valueMap=resource.adaptTo(ValueMap.class);
//

//        for(Map.Entry<String, Object> e : valueMap.entrySet()) {
//
//            w.write(e.getKey()+" "+e.getValue()+"\n");
//        }


//        w.write("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">");
//        w.write("<html>");
//        w.write("<head>");
//        w.write("<title>Hello World Servlet</title>");
//        w.write("</head>");
//        w.write("<body>");
//        w.write("<h1>Hello World!</h1>");
//        w.write("</body>");
//        w.write("</html>");
//
//        log.info("Hello World Servlet");


        } catch (DocumentException e) {
            e.printStackTrace();
        }

       document.close();
        pdfWriter.close();

    }


}

