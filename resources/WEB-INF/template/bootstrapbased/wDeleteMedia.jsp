<%@page contentType="text/html;charset=utf-8" language="java" %>
<!-- Delete Media POPUP -->
<script type="text/ng-template" id="deleteMediaPopup.html">

<div class="modal-content">

      <div class="modal-header">
      <button type="button" ng-click="$close();" class="close" data-dismiss="modal" aria-label="Schließen" title="Schliessen"><span aria-hidden="true">&times;</span></button>
      <span class="modal-title" id="meinModalLabel">Löschen</span>
      </div>

      <div class="md-modal-body">
      <!-- INHALT MODAL -->
          <div class="container-fluid"><!-- container -->
          <div class="row"><!-- row -->
              <div class="col-md-12">
                <form>
                  <div class="form-group">
                    <label>{{selectedMedia}} Datei(en) wirklich löschen?</label>
                  </div>
                </form>
              </div>
          </div><!-- /row -->
          </div><!-- /container -->
      <!-- /INHALT MODAL -->
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" type="button" ng-click="deleteMedia();$close();">JA</button>
        <button class="btn" type="button" ng-click="$close();">NEIN</button>
      </div>
</div>
<!-- /modal -->
<!-- /MODAL PARKING ####################################################################################################################################### -->
<!-- ###################################################################################################################################################### -->
</script>
<!-- Ende Delete Media POPUP -->