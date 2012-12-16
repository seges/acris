package sk.seges.sesam.remote.jmx.domain;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

import sk.seges.sesam.remote.domain.RemoteCommand;
import sk.seges.sesam.remote.domain.Response;
import sk.seges.sesam.remote.jmx.FieldExport;

/**
 * @author eldzi
 */
public class InvocationInfo implements Serializable {
    private static final long serialVersionUID = -2533104225706338771L;
    @FieldExport(index = 0, description = "Invocation collection sequence")
    private int id;
    
    @FieldExport
    private String clientHost;
    @FieldExport
    private String command;
    @FieldExport
    private String response;

    @FieldExport
    private Date invocationStart;
    @FieldExport
    private Date invocationEnd;
    @FieldExport
    private long invocationDuration;
    @FieldExport(index = 1, description = "Unique invocation identifier")
    private String uuid;
    @FieldExport(description = "Invocation side")
    private String side;

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(InetAddress clientHost) {
        this.clientHost = clientHost.getHostAddress();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(RemoteCommand command) {
        this.command = command.toString();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response.toString();
    }

    public Date getInvocationStart() {
        return invocationStart;
    }

    public void setInvocationStart(Date invocationStart) {
        this.invocationStart = invocationStart;
    }

    public Date getInvocationEnd() {
        return invocationEnd;
    }

    public void setInvocationEnd(Date invocationEnd) {
        this.invocationEnd = invocationEnd;
    }

    public long getInvocationDuration() {
        return invocationDuration;
    }

    public void setInvocationDuration(long invocationDuration) {
        this.invocationDuration = invocationDuration;
    }

    public Object[] toObjects() {
        return new Object[] { clientHost, command, response, invocationStart, invocationEnd, invocationDuration };
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
