<<<<<<< Updated upstream:Assignment-3/Server/src/main/java/bgu/spl/net/impl/networkProtocol/Task/AckMessage.java
package bgu.spl.net.impl.networkProtocol.Task;
=======
package bgu.spl.net.impl.networkProtocol.Operation;
>>>>>>> Stashed changes:Assignment-3/Server/src/main/java/bgu/spl/net/impl/networkProtocol/Operation/AckMessage.java

public class AckMessage {

    private int opCode;

    public AckMessage(int opCode){
        this.opCode = opCode;
    }

    @Override
    public String toString() {
        return "ACK " + opCode;
    }
}
