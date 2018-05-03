package com.stumpner.mediadesk.web.tags;

import com.stumpner.mediadesk.media.MediaObject;
import com.stumpner.mediadesk.core.Resources;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import org.springframework.web.util.ExpressionEvaluationUtils;
import org.apache.log4j.Logger;

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
 * User: franzstumpner
 * Date: 11.05.2005
 * Time: 21:08:47
 * To change this template use File | Settings | File Templates.
 */
public class ImageSelectorTag extends TagSupport {

    private int ifSelected = -1;
    private int ifNotSelected = -1;

    public void setIfSelected(String ifSelected) {
        try {
            this.ifSelected = ExpressionEvaluationUtils.evaluateInteger("ifSelected",ifSelected,pageContext);
        } catch (JspException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void setIfNotSelected(String ifNotSelected) {
        try {
            this.ifNotSelected = ExpressionEvaluationUtils.evaluateInteger("ifNotSelected",ifNotSelected,pageContext);
        } catch (JspException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int doStartTag() throws JspException {

        Logger logger = Logger.getLogger(ImageSelectorTag.class);

        List selectedImages = new LinkedList();
        logger.debug("ImageSelectorTag: getting imageList from SessionVar: "+Resources.SESSIONVAR_SELECTED_IMAGES);
        if (pageContext.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES)!=null) {
            selectedImages = (List)pageContext.getSession().getAttribute(Resources.SESSIONVAR_SELECTED_IMAGES);
            logger.debug("ImageSelectorTag: selectedImages="+selectedImages);
        } else {
            logger.warn("ImageSelectorTag: selectedImages is NULL, saving empty list in Session!"+selectedImages);
            pageContext.getSession().setAttribute(Resources.SESSIONVAR_SELECTED_IMAGES,selectedImages);
        }

        if (ifNotSelected!=-1) {
            if (imageIdNotInList(ifNotSelected,selectedImages)) {
                return TagSupport.EVAL_BODY_INCLUDE;
            } else {
                return TagSupport.SKIP_BODY;
            }
        }
        if (ifSelected!=-1) {
            if (imageIdInList(ifSelected,selectedImages)) {
                return TagSupport.EVAL_BODY_INCLUDE;
            } else {
                return TagSupport.SKIP_BODY;
            }
        }

        return SKIP_BODY;
        //return super.doStartTag();    //To change body of overridden methods use File | Settings | File Templates.
    }

    private boolean imageIdInList(int imageId,List imageList) {

        Logger logger = Logger.getLogger(ImageSelectorTag.class);

        Iterator images = imageList.iterator();
        while (images.hasNext()) {
            MediaObject image = (MediaObject)images.next();
            if (image!=null) {
                if (image.getIvid()==imageId) {
                    //image ist in der liste
                    return true;
                }
            }
        }
        //image is not in list
        return false;
    }

    private boolean imageIdNotInList(int imageId,List imageList) {

        Logger logger = Logger.getLogger(ImageSelectorTag.class);
        Iterator images = imageList.iterator();

        while (images.hasNext()) {
            MediaObject image = (MediaObject)images.next();
            if (image!=null) {
                if (image.getIvid()==imageId) {
                    //not its in list
                    return false;
                }
            }
        }
        //yes its not in list
        return true;
    }
}
