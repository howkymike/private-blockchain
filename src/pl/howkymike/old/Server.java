//import pl.howkymike.model.Block;
//import pl.howkymike.model.Blockchain;
//
//import javax.jws.WebService;
//
//
//@WebService(serviceName = "Project3ServerTask2")
//public class Server {
//    Blockchain blockChain;
//    Block gen;
//    static int index = 0;
//
//    @WebMethod(operationName = "Genesis")
//    public boolean Genesis() {
//
//        gen = new Block();
//        blockChain = new BlockChain();
//
//        //Adding genesis block to the chain
//        blockChain.addBlock(gen);
//        blockChain.chainHash = gen.proofOfWork(gen.difficulty);
//
//        return true;
//
//    }
//
//    @WebMethod(operationName = "convertToDoc")
//    public String convertToDoc(@WebParam(name = "xmlInput") String xmlInput) throws ParserConfigurationException, SAXException, IOException, NoSuchAlgorithmException
//    {
//        // get the needed field from the xml request body
//        org.w3c.dom.Document doc = gen.getDocument(xmlInput);
//        doc.getDocumentElement().normalize();
//
//        String root = doc.getDocumentElement().getNodeName();
//
//        NodeList nodeList = doc.getElementsByTagName(root);
//        Node node = nodeList.item(0);
//
//        Element element = (Element) node;
//
//        String operation = element.getElementsByTagName("Operation").item(0).getTextContent();
//
//        String result = "";
//
//        // implement  operations based on the operation field in the xml file
//        switch (operation) {
//            case "addBlock": {
//                String data = element.getElementsByTagName("data").item(0).getTextContent();
//                int difficulty = Integer.parseInt(element.getElementsByTagName("difficulty").item(0).getTextContent());
//
//                String resultData = gen.validateBlock(data);
//
//                if (resultData != null) {
//
//                    Block block = new Block(++index, blockChain.getTime(),data,difficulty);
//
//                    blockChain.addBlock(block);
//                    result = "Block added to the block chain";
//
//                }
//
//                else
//                    result = "Failed to add block to the block chain as data seems to be tampered";
//
//                break;
//            }
//            case "getList": {
//
//                result = blockChain.toString();
//                break;
//            }
//
//            case "validate": {
//
//
//                result = "Chain Verification "+blockChain.isChainValid();
//
//
//                break;
//            }
//
//        }
//
//        return result;
//
//    }
//}
