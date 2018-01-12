/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 07.09.2007
 * Time: 17:21:28
 * To change this template use File | Settings | File Templates.
 */
package com.stumpner.mediadesk.web.mvc.common;

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

public class MediaMenu {

    boolean visible = false;

    String actionIconCls = "";

    boolean action = false;
    boolean actionUpload = false;
    boolean actionEditCat = false;
    boolean actionEditPin = false;
    boolean actionNewCat = false;
    boolean actionDeleteCat = false;
    boolean actionRemoveMediaFromPin = false;
    boolean actionPinLink = false;

    /*
    boolean delete = false;
    boolean deleteFromDB = false;
    boolean deleteFromCategory = false;
    boolean deleteFromFolder = false;
    */

    boolean deleteAll = false;
    boolean deleteFromLightbox = false;
    boolean deleteFromShoppingcart = false;

    boolean deleteFromPin = false;

    boolean selection = false;
    boolean selectionMarkAll = false;
    boolean selectionMarkSite = false;
    boolean selectionUnmarkAll = false;
    boolean selectionAsCatImage = false;
    boolean selectionCopy = false;
    boolean selectionMove = false;
    boolean selectionToPin = false;
    boolean selectionToShoppingcart = false;
    boolean selectionFromCategory = false;
    boolean allToShoppingcart = false;

    boolean selectionRemoveMedia = false; //von Kategorie, Pin,...
    boolean selectionDeleteMedia = false;

    boolean downloadSelected = false;
    boolean downloadPin = false;

    boolean view = false;
    boolean share = false;
    boolean newFile = false;

    boolean bulkModification = false;

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public boolean isActionUpload() {
        return actionUpload;
    }

    public void setActionUpload(boolean actionUpload) {
        this.actionUpload = actionUpload;
    }

    public boolean isActionEditCat() {
        return actionEditCat;
    }

    public void setActionEditCat(boolean actionEditCat) {
        this.actionEditCat = actionEditCat;
    }

    public boolean isActionEditPin() {
        return actionEditPin;
    }

    public void setActionEditPin(boolean actionEditPin) {
        this.actionEditPin = actionEditPin;
    }

    public boolean isActionNewCat() {
        return actionNewCat;
    }

    public void setActionNewCat(boolean actionNewCat) {
        this.actionNewCat = actionNewCat;
    }

    public boolean isActionDeleteCat() {
        return actionDeleteCat;
    }

    public void setActionDeleteCat(boolean actionDeleteCat) {
        this.actionDeleteCat = actionDeleteCat;
    }

    public boolean isSelectionRemoveMedia() {
        return selectionRemoveMedia;
    }

    public void setSelectionRemoveMedia(boolean selectionRemoveMedia) {
        this.selectionRemoveMedia = selectionRemoveMedia;
    }

    public boolean isActionRemoveMediaFromPin() {
        return actionRemoveMediaFromPin;
    }

    public void setActionRemoveMediaFromPin(boolean actionRemoveMediaFromPin) {
        this.actionRemoveMediaFromPin = actionRemoveMediaFromPin;
    }

    public boolean isSelectionDeleteMedia() {
        return selectionDeleteMedia;
    }

    public void setSelectionDeleteMedia(boolean selectionDeleteMedia) {
        this.selectionDeleteMedia = selectionDeleteMedia;
    }

    public boolean isDeleteFromPin() {
        return deleteFromPin;
    }

    public void setDeleteFromPin(boolean deleteFromPin) {
        this.deleteFromPin = deleteFromPin;
    }

    public boolean isSelection() {
        return selection;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }

    public boolean isSelectionMarkAll() {
        return selectionMarkAll;
    }

    public void setSelectionMarkAll(boolean selectionMarkAll) {
        this.selectionMarkAll = selectionMarkAll;
    }

    public boolean isSelectionUnmarkAll() {
        return selectionUnmarkAll;
    }

    public void setSelectionUnmarkAll(boolean selectionUnmarkAll) {
        this.selectionUnmarkAll = selectionUnmarkAll;
    }

    public boolean isSelectionAsCatImage() {
        return selectionAsCatImage;
    }

    public void setSelectionAsCatImage(boolean selectionAsCatImage) {
        this.selectionAsCatImage = selectionAsCatImage;
    }

    public boolean isSelectionCopy() {
        return selectionCopy;
    }

    public void setSelectionCopy(boolean selectionCopy) {
        this.selectionCopy = selectionCopy;
    }

    public boolean isSelectionMove() {
        return selectionMove;
    }

    public void setSelectionMove(boolean selectionMove) {
        this.selectionMove = selectionMove;
    }

    public boolean isSelectionToPin() {
        return selectionToPin;
    }

    public void setSelectionToPin(boolean selectionToPin) {
        this.selectionToPin = selectionToPin;
    }

    public boolean isDownloadSelected() {
        return downloadSelected;
    }

    public void setDownloadSelected(boolean downloadSelected) {
        this.downloadSelected = downloadSelected;
    }

    public boolean isDownloadPin() {
        return downloadPin;
    }

    public void setDownloadPin(boolean downloadPin) {
        this.downloadPin = downloadPin;
    }

    public boolean isBulkModification() {
        return bulkModification;
    }

    public void setBulkModification(boolean bulkModification) {
        this.bulkModification = bulkModification;
    }

    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }

    public boolean isDeleteFromLightbox() {
        return deleteFromLightbox;
    }

    public void setDeleteFromLightbox(boolean deleteFromLightbox) {
        this.deleteFromLightbox = deleteFromLightbox;
    }

    public boolean isDeleteFromShoppingcart() {
        return deleteFromShoppingcart;
    }

    public void setDeleteFromShoppingcart(boolean deleteFromShoppingcart) {
        this.deleteFromShoppingcart = deleteFromShoppingcart;
    }

    public boolean isSelectionToShoppingcart() {
        return selectionToShoppingcart;
    }

    public void setSelectionToShoppingcart(boolean selectionToShoppingcart) {
        this.selectionToShoppingcart = selectionToShoppingcart;
    }

    public boolean isAllToShoppingcart() {
        return allToShoppingcart;
    }

    public void setAllToShoppingcart(boolean allToShoppingcart) {
        this.allToShoppingcart = allToShoppingcart;
    }

    public boolean isSelectionMarkSite() {
        return selectionMarkSite;
    }

    public void setSelectionMarkSite(boolean selectionMarkSite) {
        this.selectionMarkSite = selectionMarkSite;
    }

    public boolean isSelectionFromCategory() {
        return selectionFromCategory;
    }

    public void setSelectionFromCategory(boolean selectionFromCategory) {
        this.selectionFromCategory = selectionFromCategory;
    }

    public boolean isActionPinLink() {
        return actionPinLink;
    }

    public void setActionPinLink(boolean actionPinLink) {
        this.actionPinLink = actionPinLink;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getActionIconCls() {
        return actionIconCls;
    }

    public void setActionIconCls(String actionIconCls) {
        this.actionIconCls = actionIconCls;
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean isNewFile() {
        return newFile;
    }

    public void setNewFile(boolean newFile) {
        this.newFile = newFile;
    }
}
