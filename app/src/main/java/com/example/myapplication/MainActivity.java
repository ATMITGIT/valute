package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    GridView grid;
List<String> nodelist=new ArrayList<>();
String usd,eur;
TextView result;
EditText e;
    String[] valute = {"USD", "EUR", "RUR"};
    Spinner spinner,spinner1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.start);
        spinner1 = findViewById(R.id.end);
        result=findViewById(R.id.result);
        e=findViewById(R.id.editText);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valute);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner1.setAdapter(adapter1);

        grid= findViewById(R.id.gridview);
        nodelist.add("Букв. код");
        nodelist.add("Единиц");
        nodelist.add("Курс");






        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.cbr.ru/scripts/XML_daily.asp";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            String str = new String(response.getBytes("UTF-8") );
                            InputStream is = new
                                    ByteArrayInputStream(str.getBytes("UTF-8"));

                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            Document doc = dBuilder.parse(is);

                            Element element=doc.getDocumentElement();
                            element.normalize();

                            NodeList nList = doc.getElementsByTagName("Valute");

                            for (int i=0; i<nList.getLength(); i++) {

                                Node node = nList.item(i);
                                if (node.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element2 = (Element) node;
if(getValue("CharCode", element2).equals("USD")) {
    usd=getValue("Value", element2);
    nodelist.add(getValue("CharCode", element2));
    nodelist.add(getValue("Nominal", element2));
    nodelist.add(usd);
}
else if(getValue("CharCode", element2).equals("EUR"))
{
    eur=getValue("Value", element2);
    nodelist.add(getValue("CharCode", element2));
    nodelist.add(getValue("Nominal", element2));
    nodelist.add(eur);
}
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1, nodelist);
                            grid.setAdapter(adapter);

                        } catch (Exception e) {
                            Log.d("Exception",e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"ResponseError",Toast.LENGTH_SHORT).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    public void click(View v)
    {
        String text = spinner.getSelectedItem().toString();
        String text1 = spinner1.getSelectedItem().toString();
        String number=e.getText().toString();
        eur=eur.replace(",",".");
        usd=usd.replace(",",".");
        if(!number.equals("")) {
            if (text.equals(text1)) {
                result.setText(e.getText().toString() + " " + text1);
            } else if (text.equals("EUR") && text1.equals("RUR")) {
               Double res=Double.parseDouble(number)*Double.parseDouble(eur);
               result.setText(res+" "+text1);
            } else if (text.equals("EUR") && text1.equals("USD")) {
                Double res=Double.parseDouble(number)*Double.parseDouble(eur);
                Double res1=res/Double.parseDouble(usd);
                result.setText(res1+" "+text1);
            } else if (text.equals("USD") && text1.equals("RUR")) {
                Double res=Double.parseDouble(number)*Double.parseDouble(usd);
                result.setText(res+" "+text1);
            } else if (text.equals("USD") && text1.equals("EUR")) {
                Double res=Double.parseDouble(number)*Double.parseDouble(usd);
                Double res1=res/Double.parseDouble(eur);
                result.setText(res1+" "+text1);
            } else if (text.equals("RUR") && text1.equals("EUR")) {
                Double res=Double.parseDouble(number)/Double.parseDouble(eur);
                result.setText(res+" "+text1);
            } else if (text.equals("RUR") && text1.equals("USD")) {
                Double res=Double.parseDouble(number)/Double.parseDouble(usd);
                result.setText(res+" "+text1);
            }
        }
    }

}
