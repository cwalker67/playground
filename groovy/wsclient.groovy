import groovyx.net.ws.WSClient

proxy = new WSClient("http://localhost:7001/OrderManagerService/?WSDL", this.class.classLoader)
proxy.initialize()
new File("orderIds.txt").eachLine{orderId -> proxy.processOrder(orderId)}
