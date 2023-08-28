package ch.vd.tools.ig2fedi.connector.mastodon;

import ch.vd.tools.ig2fedi.connector.mastodon.model.MediaAttachement;
import ch.vd.tools.ig2fedi.connector.mastodon.model.Status;

import java.util.List;

public interface MastodonConnector {

    MediaAttachement uploadMedia(String mediaUrl);

    Status publishPost(String title, List<String> mediaUrls);
}
