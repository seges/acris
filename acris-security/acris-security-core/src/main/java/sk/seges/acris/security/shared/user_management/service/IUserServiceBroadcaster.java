package sk.seges.acris.security.shared.user_management.service;

import sk.seges.acris.security.shared.session.ClientSessionDTO;

/**
 * Created by PeterSimun on 21.5.2014.
 */
public interface IUserServiceBroadcaster extends IUserServiceAsync {

    void addUserService(IUserServiceAsync userService);

    void setClientSession(ClientSessionDTO clientSession);

    void setPrimaryEntryPoint(String primaryEntryPoint);
}