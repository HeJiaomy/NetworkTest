package com.example.networktest;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX解析xml文件
 */

public class ContentHandler extends DefaultHandler {
    private String nodeName;

    private StringBuilder id,name,versioin;

    @Override
    public void startDocument() throws SAXException {
        id= new StringBuilder();
        name= new StringBuilder();
        versioin= new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //记录当前节点名
        nodeName= localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //根据当前的节点名判断将内容添加到哪个StringBuilder中
        if ("id".equals(nodeName)){
            id.append(ch, start, length);
        }else if ("name".equals(nodeName)){
            name.append(ch,start,length);
        }else if ("version".equals(nodeName)){
            versioin.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("app".equals(localName)){
            Log.d("ContentHandler","id is:"+id.toString().trim());
            Log.d("ContentHandler","name is:"+name.toString().trim());
            Log.d("ContentHandler","version is:"+versioin.toString().trim());

            id.setLength(0);
            name.setLength(0);
            versioin.setLength(0);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }
}
