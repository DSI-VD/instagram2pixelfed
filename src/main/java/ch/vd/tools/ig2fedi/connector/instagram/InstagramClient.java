package ch.vd.tools.ig2fedi.connector.instagram;

import ch.vd.tools.ig2fedi.connector.instagram.model.PostInfo;

import java.util.List;

public interface InstagramClient {

    String getAccountKey(String username);

    List<PostInfo> getLastPosts(String username, int count);
}
