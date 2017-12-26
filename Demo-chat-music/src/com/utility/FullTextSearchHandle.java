package com.utility;

import com.entity.Song;
import com.google.appengine.api.search.*;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FullTextSearchHandle {

    private static Logger logger = Logger.getLogger(FullTextSearchHandle.class.getSimpleName());
    private static IndexSpec indexSpec = IndexSpec.newBuilder().setName("songFullTextSearch").build();
    private static Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);

    public static FullTextSearchHandle getInstance(){
        return new FullTextSearchHandle();
    }

    public static void add(Document document) {
        try {
            index.put(document);
            logger.info("Index success");
        } catch (PutException e) {
            logger.severe("Index error");
            e.printStackTrace(System.err);
        }
    }

    public static void delete(String id) {
        try {
            index.delete(id);
            logger.info("Remove index success.");
        } catch (PutException e) {
            logger.severe("Remove index error");
            e.printStackTrace(System.err);
        }
    }

    public List<Song> search(String keyword) {
        List<Song> listSongs = new ArrayList<>();
        Results<ScoredDocument> results = index.search(keyword);
        if (results != null && results.getNumberReturned() > 0) {
            for (ScoredDocument document : results) {
                try{
                  Song song =  new Song();

                    song.setId(document.getId());
                    song.setName(document.getOnlyField("name").getText());
                    song.setSinger(document.getOnlyField("singer").getText());
                    listSongs.add(song);
                }catch(Exception ex) {
                    logger.severe("Convert document lỗi.");
                    ex.printStackTrace(System.err);
                }
            }
        }
        return listSongs;
    }
}
