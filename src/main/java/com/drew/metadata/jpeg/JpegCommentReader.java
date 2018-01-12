/*
 * This is public domain software - that is, you can do whatever you want
 * with it, and include it software that is licensed under the GNU or the
 * BSD license, or whatever other licence you choose, including proprietary
 * closed source licenses.  I do ask that you leave this header in tact.
 *
 * If you make modifications to this code that you think would benefit the
 * wider community, please send me a copy and I'll post it on my site.
 *
 * If you make use of this code, I'd appreciate hearing about it.
 *   drew@drewnoakes.com
 * Latest version of this software kept at
 *   http://drewnoakes.com/
 *
 * Created by dnoakes on Oct 10, 2003 using IntelliJ IDEA.
 */
package com.drew.metadata.jpeg;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Drew Noakes http://drewnoakes.com
 */
public class JpegCommentReader implements MetadataReader
{

    private String encoding = null;

    /**
     * The COM data segment.
     */
    private final byte[] _data;

    /**
     * Creates a new JpegReader for the specified Jpeg jpegFile.
     */
    public JpegCommentReader(File jpegFile) throws JpegProcessingException
    {
        this(new JpegSegmentReader(jpegFile).readSegment(JpegSegmentReader.SEGMENT_COM));
    }

    /** Creates a JpegCommentReader for a JPEG stream.
     *
     * @param is JPEG stream. Stream will be closed.
     */
    public JpegCommentReader(InputStream is) throws JpegProcessingException
    {
        this(new JpegSegmentReader(is).readSegment(JpegSegmentReader.SEGMENT_APPD));
    }

    public JpegCommentReader(byte[] data)
    {
        _data = data;
    }

    /**
     * Added by franz.stumpner
     * @param data
     */
    public JpegCommentReader(byte[] data,String encoding)
    {
        _data = data;
        this.encoding = encoding;
    }

    /**
     * Performs the Jpeg data extraction, returning a new instance of <code>Metadata</code>.
     */
    public Metadata extract()
    {
        return extract(new Metadata());
    }

    /**
     * Performs the Jpeg data extraction, adding found values to the specified
     * instance of <code>Metadata</code>.
     */
    public Metadata extract(Metadata metadata)
    {
        //System.out.println("Read JpegComment-extract");

        if (_data==null) {
            //System.out.println("_data = null");
            return metadata;
        }

        JpegCommentDirectory directory = (JpegCommentDirectory)metadata.getDirectory(JpegCommentDirectory.class);

        if (encoding==null) {
            directory.setString(JpegCommentDirectory.TAG_JPEG_COMMENT, new String(_data));
        } else {
            try {
                //System.out.println("Special Encoding:" + encoding);
                directory.setString(JpegCommentDirectory.TAG_JPEG_COMMENT, new String(_data,encoding));
            } catch (UnsupportedEncodingException e) {
                System.out.println("Encoding exception "+encoding);
                directory.setString(JpegCommentDirectory.TAG_JPEG_COMMENT, new String(_data));
            }
        }

        return metadata;
    }
}
