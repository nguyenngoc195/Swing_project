package com.utility;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UpLoadHandle extends HttpServlet {

    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private ImagesService imagesService = ImagesServiceFactory.getImagesService();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("myFile");
        if (blobKeys == null || blobKeys.isEmpty()) {

            resp.getWriter().println("Error");
        } else {
            ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0)).secureUrl(true);

            imagesService.getServingUrl(options);
            //            resp.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
            resp.getWriter().println(imagesService.getServingUrl(options));
        }

    }

}
