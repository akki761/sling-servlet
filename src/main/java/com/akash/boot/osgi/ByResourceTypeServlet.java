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

import java.io.IOException;
import java.io.Writer;
import java.util.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;
//
///**
// * Hello World Servlet registered by resource type
// *
// * Annotations below are short version of:
// *
// * @Component
// * @Service(Servlet.class)
// * @Properties({
// *    @Property(name="service.description", value="Hello World Type Servlet"),
// *    @Property(name="service.vendor", value="The Apache Software Foundation"),
// *    @Property(name="sling.servlet.resourceTypes", value="sling/servlet/default"),
// *    @Property(name="sling.servlet.selectors", value="hello"),
// *    @Property(name="sling.servlet.extensions", value="html")
// * })
// */
//@SlingServlet(resourceTypes="sling/servlet/default", selectors="hello", extensions="html")
//@Properties({
//    @Property(name="service.description", value="Hello World Type Servlet"),
//    @Property(name="service.vendor", value="The Apache Software Foundation")
//})


@Component(
        service = {Servlet.class},
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=blog"
        }
)
@SuppressWarnings("serial")
public class ByResourceTypeServlet extends SlingSafeMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(ByResourceTypeServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request,
            SlingHttpServletResponse response) throws ServletException,
            IOException {

        String req=request.getParameter("orderby");
        Resource resource = request.getResource();
        Iterator<Resource> resourceIterator=resource.listChildren();
        //    ArrayList<Resource> resourceArrayList=new ArrayList<>();
        Writer w=response.getWriter();
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
            w.write(r.getName()+"  "+r.getValueMap().get("jcr:created","default")+"\n");
        }
//        w.write("<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">");
//        w.write("<html>");
//        w.write("<head>");
//        w.write("<title>Hello World Servlet</title>");
//        w.write("</head>");
//        w.write("<body>");
//        w.write("<h1>Hello ");
//        w.write(resource.getPath());
//        w.write("</h1>");
//        w.write("</body>");
//        w.write("</html>");
//
//        log.info("Hello World Servlet");

    }

}

