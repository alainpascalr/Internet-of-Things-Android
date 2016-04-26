package neutronsoftwareinc.internetofthings;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by HamidHabib on 16-04-06.
 */
public class XMLParser extends AppCompatActivity {
    PlaceHolderFragment taskFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.xml_parser);

        if(savedInstanceState == null){
            taskFragment = new PlaceHolderFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(taskFragment, "MyFragment").commit();
        } else {
            taskFragment = (PlaceHolderFragment) getSupportFragmentManager()
                    .findFragmentByTag("MyFragment");
        }
        taskFragment.startTask();
    }

    public static class PlaceHolderFragment extends Fragment {
        TechCrunchTask downloadTask;
        public PlaceHolderFragment(){}

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }

        public void startTask(){
            if(downloadTask != null){
                downloadTask.cancel(true);
            } else {
                downloadTask = new TechCrunchTask();
                downloadTask.execute();
            }
        }
    }

    public static class TechCrunchTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            //create object of bluetooth device, get the mac address of the bluetooth device. call getinputstream on the bluetooth object
            //that should give us the inputstream where we receive the data. and then call the processXML method on the inputStream.
            String downloadURL = "http://feeds.feedburner.com/techcrunch/android?format=xml";
            try {
                URL url = new URL(downloadURL);
                HttpURLConnection connection = (HttpURLConnection)
                        url.openConnection();

                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                processXML(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void processXML(InputStream inputStream) throws Exception{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputStream);
            Element rootElement = xmlDocument.getDocumentElement();
            NodeList itemsList = rootElement.getElementsByTagName("item");
            NodeList itemChildren = null;
            Node currentItem = null;
            Node currentChild = null;
            //for the image from xlm
//            NamedNodeMap mediaThumbnailAttributes;
//            Node currentAttribute = null;
//            int count = 0;
            for(int i = 0; i< itemsList.getLength(); i ++){
                currentItem = itemsList.item(i);
                itemChildren = currentItem.getChildNodes();
                for(int j =0; j<itemChildren.getLength(); j++){

                    currentChild = itemChildren.item(j);

                    //every item tag it prints title tag information
                    if(currentChild.getNodeName().equalsIgnoreCase("title")){
                        L.m(currentChild.getTextContent());
                    }
                    if(currentChild.getNodeName().equalsIgnoreCase("pubDate")){
                        L.m(currentChild.getTextContent());
                    }
                    if(currentChild.getNodeName().equalsIgnoreCase("description")){
                        L.m(currentChild.getTextContent());
                    }

//                    if(currentChild.getNodeName().equalsIgnoreCase("media:thumbnail")){
////                        mediaThumbnailAttributes = currentChild.getAttributes();
////                        for(int k =0; k<mediaThumbnailAttributes.getLength(); k++ ){
////                            currentAttribute = mediaThumbnailAttributes.item(k);
////                            if (currentAttribute.getNodeName().equalsIgnoreCase("url")){
////                                L.m(currentAttribute.getTextContent());
////                            }
////                        }
//                        count++;
//                       L.m(currentChild.getAttributes().item(0).getTextContent());
//
//                    }
                }
            }
        }
    }
}
