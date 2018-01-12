package com.stumpner.mediadesk.stats.dsp;
import java.io.Serializable;
import java.util.*;

import org.apache.log4j.Logger;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import com.stumpner.mediadesk.core.database.sc.DownloadLoggerService;
import com.stumpner.mediadesk.core.database.sc.UserService;
import com.stumpner.mediadesk.core.database.sc.exceptions.ObjectNotFoundException;
import com.stumpner.mediadesk.core.database.sc.exceptions.IOServiceException;
import com.stumpner.mediadesk.stats.XyMap;
import com.stumpner.mediadesk.usermanagement.User;

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
 * An example data producer.
 * @author  Guido Laures
 */
public class DownloadedPics implements DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {

    private static final Logger log = Logger.getLogger(PageViewCountData.class);

    // These values would normally not be hard coded but produced by
    // some kind of data source like a database or a file
    private final String[] categories =    {"mon", "tue", "wen", "thu", "fri", "sat", "sun"};
    private final String[] seriesNames =    {"cewolfset.jsp"};
    private int interval = 0;
    private String[] drill = null;
    private int downloadType = 1;

    /**
	 *  Produces some random data.
	 */
    public Object produceDataset(Map params) throws DatasetProduceException {
    	log.debug("producing data.");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			/**
			 * @see java.lang.Object#finalize()
			 */
			protected void finalize() throws Throwable {
				super.finalize();
				log.debug(this +" finalized.");
			}
        };

        if (drill==null) {

            //kein Drill: alle Bilder anzeigen

            DownloadLoggerService dls = new DownloadLoggerService();
            List xyValues = dls.getAllDownloadedPics(interval,0,downloadType);

            Iterator xyValue = xyValues.iterator();


            while (xyValue.hasNext()) {

                XyMap xymap = (XyMap)xyValue.next();
                dataset.addValue(xymap.getY(),"",xymap.getX());
            }

        } else {

            //userdrill
            for (int p=0;p<drill.length;p++) {

                int userId = Integer.parseInt(drill[p]);
                UserService userService = new UserService();
                try {
                    User user = (User)userService.getById(userId);

                    DownloadLoggerService dls = new DownloadLoggerService();
                    List xyValues = dls.getAllDownloadedPics(interval,userId,downloadType);
                    Iterator xyValue = xyValues.iterator();

                    while (xyValue.hasNext()) {

                        XyMap xymap = (XyMap)xyValue.next();
                        dataset.addValue(xymap.getY(),user.getUserName(),xymap.getX());
                    }


                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                } catch (IOServiceException e) {
                    e.printStackTrace();
                }

            }

        }
        /*
        for (int i = 0; i < categories.length; i++) {
            final int y = lastY + (int)(Math.random() * 200 - 100);
            lastY = y;
            dataset.addValue(y, seriesNames[series], categories[i]);
        }
        */

        return dataset;
    }

    /**
     * This producer's data is invalidated after 5 seconds. By this method the
     * producer can influence Cewolf's caching behaviour the way it wants to.
     */
	public boolean hasExpired(Map params, Date since) {
        log.debug(getClass().getName() + "hasExpired()");
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}

	/**
	 * Returns a unique ID for this DatasetProducer
	 */
	public String getProducerId() {
		return "DownloadedPics DatasetProducer";
	}

    /**
     * Returns a link target for a special data item.
     */
    public String generateLink(Object data, int series, Object category) {
        return seriesNames[series];
    }

	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
		log.debug(this + " finalized.");
	}

	/**
	 * @see org.jfree.chart.tooltips.CategoryToolTipGenerator#generateToolTip(CategoryDataset, int, int)
	 */
	public String generateToolTip(CategoryDataset arg0, int series, int arg2) {
		return seriesNames[series];
	}

    public void setInterval(String interval) {
        Logger logger = Logger.getLogger(getClass());
        logger.debug("Filter settet: "+interval);
        if (interval!=null) {
            this.interval = Integer.parseInt(interval);
        }
    }

    public void setDrill(String[] drill) {
        this.drill = drill;
    }

    public void setDownloadType(int downloadType) {
        //System.out.println("DownloadType: "+downloadType);
        this.downloadType = downloadType;
    }
}