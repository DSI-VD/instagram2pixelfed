package ch.vd.tools.ig2fedi.service;

import ch.vd.tools.ig2fedi.configuration.InstagramProperties;
import ch.vd.tools.ig2fedi.configuration.MastodonProperties;
import ch.vd.tools.ig2fedi.connector.instagram.InstagramClient;
import ch.vd.tools.ig2fedi.connector.instagram.model.PostInfo;
import ch.vd.tools.ig2fedi.connector.instagram.model.edge.EdgeResult;
import ch.vd.tools.ig2fedi.connector.mastodon.MastodonConnector;
import ch.vd.tools.ig2fedi.util.FileBackedPersistentList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class CrossPostingService {

    private final InstagramClient instagramClient;
    private final MastodonConnector mastodonConnector;
    private final InstagramProperties instagramProperties;
    private final MastodonProperties mastodonProperties;
    private final List<String> postedPosts;

    public CrossPostingService(
            InstagramClient instagramClient,
            MastodonConnector mastodonConnector,
            InstagramProperties instagramProperties,
            MastodonProperties mastodonProperties
    ) {
        this.instagramClient = instagramClient;
        this.mastodonConnector = mastodonConnector;
        this.instagramProperties = instagramProperties;
        this.mastodonProperties = mastodonProperties;
        this.postedPosts = new FileBackedPersistentList<>(mastodonProperties.cacheFile());
    }

    public void crossPost() {
        log.info("Starting cross posting");
        var accountKey = instagramClient.getAccountKey(instagramProperties.targetUsername());
        var posts = instagramClient.getLastPosts(accountKey, instagramProperties.postCount());
        posts.forEach(this::handlePost);
    }

    private void handlePost(PostInfo post) {
        if (postedPosts.contains(post.getShortcode())) {
            log.info("Post %s already posted, skipping".formatted(post.getShortcode()));
            return;
        }

        log.info("Posting post %s".formatted(post.getShortcode()));
        var mediaUrls = getMediaUrls(post);
        mediaUrls = mediaUrls.subList(0, Math.min(mediaUrls.size(), mastodonProperties.maxCarouselSize()));
        var caption = post.getCaption().getEdgeResult().getEdges().get(0).getNode().getText();
        mastodonConnector.publishPost(caption, mediaUrls);
        log.info("Finished posting post %s".formatted(post.getShortcode()));

        postedPosts.add(post.getShortcode());
    }

    private List<String> getMediaUrls(PostInfo postInfo) {
        return switch (postInfo.getType()) {
            case "GraphImage" -> List.of(postInfo.getDisplayUrl());
            case "GraphVideo" -> List.of(requireNonNull(postInfo.getVideoUrl()));
            case "GraphSidecar" -> requireNonNull(postInfo.getSidecarChildren())
                    .getEdgeResult().getEdges().stream()
                    .map(EdgeResult.Edge::getNode)
                    .map(PostInfo::getDisplayUrl)
                    .toList();
            default -> throw new IllegalArgumentException("Unknown graph type: %s".formatted(postInfo.getType()));
        };
    }
}
