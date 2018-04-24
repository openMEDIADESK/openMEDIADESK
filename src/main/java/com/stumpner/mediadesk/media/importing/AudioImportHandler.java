package com.stumpner.mediadesk.media.importing;

import com.stumpner.mediadesk.core.database.sc.MediaMetadataService;
import com.stumpner.mediadesk.core.database.sc.MediaService;
import com.stumpner.mediadesk.image.MediaObjectMultiLang;
import com.stumpner.mediadesk.image.util.SizeExceedException;
import com.stumpner.mediadesk.image.Metadata;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;

import java.io.File;
import java.util.List;

import com.stumpner.mediadesk.util.UploadNotificationService;
import com.stumpner.mediadesk.upload.FileRejectException;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 28.12.2011
 * Time: 07:24:47
 * To change this template use File | Settings | File Templates.
 */
public class AudioImportHandler extends AbstractAudioVideoImportHandler {

    public int processImport(File file, int userId) throws SizeExceedException, FileRejectException {

        FileInfo fileInfo = readFileInfo(file);
        long duration = getDuration(fileInfo);
        int bitrate = getBitrate(fileInfo);
        int channels = getChannels(fileInfo);
        int sampleRate = getSampleRate(fileInfo);
        List<Metadata> metadataList = getMetadataList(fileInfo);

        int ivid = super.processImport(file, userId);

        MediaService mediaService = new MediaService();
        MediaObjectMultiLang mediaObject = (MediaObjectMultiLang)mediaService.getMediaObjectById(ivid);
        mediaObject.setDuration(duration);
        mediaObject.setBitrate(bitrate);
        mediaObject.setSamplerate(sampleRate);
        mediaObject.setChannels(channels);

        //Metadaten speichern:
        MediaMetadataService mediaMetadataService = new MediaMetadataService();
        for (Metadata metadata : metadataList) {

            metadata.setIvid(mediaObject.getIvid());
            metadata.setLang("");
            metadata.setVersionId(mediaObject.getVersion());
            mediaMetadataService.addMetadata(metadata);

            //System.out.println("Metadata: "+metadata.getMetaKey()+" -> "+metadata.getMetaValue());

            //Daten (derzeit noch statisch) in das mediaObject schreiben
            if (metadata.getMetaKey().equalsIgnoreCase("TIT2")) {
                //Title
                mediaObject.setVersionTitle(metadata.getMetaValue());
                mediaObject.setVersionTitleLng1(metadata.getMetaValue());
                mediaObject.setVersionTitleLng2(metadata.getMetaValue());
            }

            if (metadata.getMetaKey().equalsIgnoreCase("TPE1")) {
                //Artist
                mediaObject.setArtist(metadata.getMetaValue());
            }

            if (metadata.getMetaKey().equalsIgnoreCase("TALB")) {
                //Album
                mediaObject.setAlbum(metadata.getMetaValue());
            }

            if (metadata.getMetaKey().equalsIgnoreCase("TRCK")) {
                //Track
            }
            
            if (metadata.getMetaKey().equalsIgnoreCase("TCON")) {
                //Genre
                mediaObject.setGenre(metadata.getMetaValue());
            }

            if (metadata.getMetaKey().equalsIgnoreCase("TCOM")) {
                //Komponist
            }

            if (metadata.getMetaKey().equalsIgnoreCase("TYER")) {
                //Jahr
            }

        }

        try {
            mediaService.saveMediaObject(mediaObject);
        } catch (IOServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        UploadNotificationService uns = new UploadNotificationService();
        uns.triggerUpload(ivid);

        return ivid;
    }
}
