package nl.avans.pelicoonmessenger.base.net.packets;

public class AuthenticationFailedPacket extends Packet {

    public enum ErrorType { TOKEN, PACKET, UNKNOWN }

    private ErrorType errorType;
    private String errorMessage;

    public AuthenticationFailedPacket(ErrorType errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public Type getPacketType() {
        return Type.SERVER;
    }
}
