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

//Folder-Treeview (Accordeon)

angular.module('ui.mediadesk', 
		[
			'ngSanitize',
			'com.2fdevs.videogular',
			'com.2fdevs.videogular.plugins.controls',
			'com.2fdevs.videogular.plugins.overlayplay',
			'com.2fdevs.videogular.plugins.poster',
			'ngAnimate',
			'ui.bootstrap',
			'ngDialog',
			'flow',
			'chart.js',
			'toaster',
			'ngtimeago',
			'ngTouch',
			'ngWig',
			'ngDragDrop'
		]);

angular.module('ui.mediadesk').controller('TreeViewCtrl', function ($scope, $http, toaster) {

    $scope.createNode = function(id,name,title,level) {
        var newNode = [];
        newNode.id = id;
        newNode.name = name;
        newNode.title = title;
        newNode.level = level;
        newNode.open = false;
		newNode.hasChilds = false;
		newNode.isLoading = false;

        return newNode;
    }

    //Controller für den Baum
    $scope.nodes = [];

    //Funktion um Child-Nodes zu laden
    $scope.loadChilds = function(parentId, level, callback) {

        //Statusmeldung bei langen laufzeiten
        if (level == 0) { $scope.treeloading = true; }

        //Nodes laden
        $http.get("/api/rest/folder/"+parentId+"/child/data.json")
        .then(function(response) {
          //alert('erfolgreich');
		    if (level == 0) { $scope.treeloading = false; }
            var apiNodes = response.data;
            for (var i=0;i<apiNodes.length;i++) {
                apiNodes[i].level = level;
            }
            callback(apiNodes);
          //$scope.groups = response.data;
        }, function(response) {
          alert('Fehler: loadChilds '+response);
        });

    }

    //Root Notes laden...
    //$scope.loadChilds(0,0, function(nodes) {
    //    $scope.nodes = nodes;
    //});

    $scope.onFolderNodeClick = function(node,index) {
        //node = aktueller Knoten der geklickt wurde
        //index = index des aktuellen knoten der geklickt wurde

        //alert('hallo node'+node.id+" length="+$scope.nodes.length+" index:"+index);

        if (node.open) {
            //Orderknoten ist offen --> schliessen
            node.open = false;

            //Elemente entfernen
            var indexCounter = index+1;
            var removeNodesCount = 0;

            while (indexCounter < $scope.nodes.length) {
                //alert('indexCounter='+indexCounter+" length="+$scope.nodes.length);
                if ($scope.nodes[indexCounter].level > node.level) {
                    //Ebene des gefundenen knoten ist höher als zu öffnender knoten (unterebene) --> entfernen
                    removeNodesCount = removeNodesCount+1;
                    //alert('remove');
                } else {
                    break;
                }
                indexCounter = indexCounter+1;
            }

            //Die gefundenen Knoten aus dem Array entfernen
            $scope.nodes.splice(index+1, removeNodesCount);

        } else {
            //Ordnerknoten ist zu --> öffnen
            node.open = true;

            //var newNode = {
            //    id: 'x',
            //    name: 'newNode',
            //    title: 'newNode',
            //    settings: 'true',
            //    level: node.level+1,
            //    open: false
            //};
            //var newNode = $scope.createNode('x','newNodeN', 'newNodeT', node.level+1);

            //Nodes des nächsten Levels laden
			console.log("node is loading "+index);
			node.isLoading = true;
            $scope.loadChilds(node.id, node.level+1, function(newNodes) {

				console.log("loading fertig"+index);
			    $scope.nodes[index].isLoading = false;
                //Knoten einfügen
                for (var a=0;a<newNodes.length;a++) {
                    //ALT: $scope.nodes.splice(index+1,0, newNodes[a]);
					$scope.nodes.splice(index+1+a,0, newNodes[a]);
                }

            })
            
        }
    }

    //Path = [1,24234,345] (Array mit Folder-IDs)
    $scope.openTreePath = function(pathIdArray) {

        //Root Notes laden...
        $scope.loadChilds(0,0, function(nodes) {
            $scope.nodes = nodes;

            //Pfad der geoeffnet werden soll aufklappen
            if (pathIdArray!=undefined) {
                if (pathIdArray.length>0) {
                    $scope.continueOpenTreePath(pathIdArray,0);
                }
            }

        });

        $scope.continueOpenTreePath = function(pathIdArray,step) {

            var idToOpen = pathIdArray[step];
            for (var b=0;b<$scope.nodes.length;b++) {

                //alert('b '+b+' node id '+$scope.nodes[b].id+' level '+$scope.nodes[b].level+" idtoopen"+idToOpen);
                if ($scope.nodes[b].level==step) {

                    //Aktueller Node befindet sich im selben Level wie ein zu öffnender Node
                    if ($scope.nodes[b].id==idToOpen) {
                        //alert('oeffnen'+$scope.nodes[b].id+" index:"+b);
                        //Als geöffnet anzeigen
                        $scope.nodes[b].open=true;
                        var indexToOpen = b;
                        //Kinder laden
                        $scope.loadChilds($scope.nodes[b].id, step+1, function(newNodes) {

                            //Knoten einfügen
                            //alert('inserting at index:'+indexToOpen);
                            for (var c=0;c<newNodes.length;c++) {
                                $scope.nodes.splice(indexToOpen+1+c,0, newNodes[c]);
                            }

                            //alert('continueOpenTreePath'+pathIdArray.length+" step: "+step);
                            if (step+1<pathIdArray.length) {
                                //alert('continue');
                                $scope.continueOpenTreePath(pathIdArray,step+1);
                            }

                        })

                    }
                }
            }

        }

    }
	
	//Tree Drag & Drop
	
    $scope.dropCallback = function(event, index, item, external, type, allowedType) {
		alert('dropped to tree index= '+index+' item'+item.id+' level'+item.level);
        //$scope.logListEvent('dropped at', event, index, external, type);
        if (external) {
            if (allowedType === 'itemType' && !item.label) return false;
            if (allowedType === 'containerType' && !angular.isArray(item)) return false;
        } 
        return item;
    };
	
    $scope.dropSuccessHandler = function($event,index,array){
		alert('drop success: '+index);
        //array.splice(index,1);
    };
      
    $scope.onDrop = function($event,$data,array){
        alert('ondrop: '+index);
		//array.push($data);
    };
	
    $scope.onTreeNodeDrop = function(node,$data,$event){
        //alert('ontreenodedrop: target.node='+node.id+" source.node="+$data.id);
		
		var nodeId = 0;
		
		if (node!=null) {
			nodeId = node.id;
		}
		
		// URL: /ajax/catreorder?node={moving.nodeid}&parent={new.parendid}
		
		if (typeof $data.ivid !== 'undefined') {
			//Medienobjekt das gedroppt wird //TODO:
			
			console.log('folderid = '+$scope.folderId);
			var copyOrMove = $event.dataTransfer.dropEffect;
			var copyOrMoveSuccessText = "";
			var copyOrMoveErrorText = "";
			
			if ($event.dataTransfer.dropEffect == 'move') {
				copyOrMoveSuccessText = "Medienobjekt verschoben";
				copyOrMoveErrorText = "Medienobjekt verschieben fehlgeschlagen";
				console.log('mediaobject move');
			}
			if ($event.dataTransfer.dropEffect == 'copy') {
				copyOrMoveSuccessText = "Medienobjekt kopiert";
				copyOrMoveErrorText = "Medienobjekt kopieren fehlgeschlagen";
				console.log('mediaobject copy');
			}
			
			console.log('mediaobject');
			//toaster.pop('error', "Nicht unterstützt", "Verschieben von medienobjekten wird dzt nicht unterstützt "+$data.ivid+" nach "+node.id);
			
			//Kopieren: /api/rest/folder/<folderid>/copy/{ivid}
			$http.get("/api/rest/folder/"+nodeId+"/"+copyOrMove+"/"+$data.ivid+"/from/"+$scope.folderId)
			.then(function(response) {

				console.log('success');
				
				toaster.pop('success', "OK", copyOrMoveSuccessText);
				
			  //$scope.groups = response.data;
			}, function(response) {
				
				toaster.pop('error', "Fehler", copyOrMoveErrorText);
			});
			
		} else {
			//Ordner der verschoben wird
			console.log('folder');
			
			if ($data.id != nodeId) {
			
				//Nodes ändern
				$http.get("/ajax/catreorder?node="+$data.id+"&parent="+nodeId+"&json=true")
				.then(function(response) {

					console.log('success');
					
					toaster.pop('success', "OK", "Ordner verschoben");
					
					console.log('after toaster');
					
					//Nodes neu laden:
					var removeNodeIndex = -1;
					for (var a=0;a<$scope.nodes.length;a++) {
						
						console.log('$scope.nodes[a].id == $data.id : '+$scope.nodes[a].id+'=='+$data.id);
						
						if ($scope.nodes[a].id == $data.id) {
							console.log('removenodeindex'+a);
							removeNodeIndex = a;
						}
						//$scope.nodes.splice(index+1,0, newNodes[a]);
					}
					if (removeNodeIndex>=0) {
						console.log('nodes splice'+removeNodeIndex);
						$scope.nodes.splice(removeNodeIndex,1);
					}
					if (node!=null) {
						if (node.open == true) { //Target Node ist offen, nochmals öffnen
							var targetNodeIndex = -1;
							for (var b=0;b<$scope.nodes.length;b++) {
								
								if ($scope.nodes[b].id == node.id) {
									targetNodeIndex = b;
								}
								//$scope.nodes.splice(index+1,0, newNodes[a]);
							}
						
							console.log('zumachen');
							$scope.onFolderNodeClick(node, targetNodeIndex);
							console.log('nochmal aufmachen');
							$scope.onFolderNodeClick(node, targetNodeIndex);
							
						//	if ($scope.nodes[b].id == $node.id) {
						//		if ($)
						//	}
							//$scope.nodes.splice(index+1,0, newNodes[a]);
						}
					} else {
						//Root Node neu laden
						$scope.openTreePath([]);
					}
					
					
					
				  //$scope.groups = response.data;
				}, function(response) {
					
					toaster.pop('error', "Fehler", "Ordner verschieben fehlgeschlagen!");
				});
			
			} else {
				
					toaster.pop('error', "Nicht möglich", "Ordner kann nicht in sich selbst verschoben werden!");
					
					console.log('folder parent = folderid (same)');
				
			}
		
		}
		
		//array.push($data);
    };
	
});

angular.module('ui.mediadesk').controller('UploadViewCtrl', function ($scope, $http, $timeout, $uibModal, $window, ngDialog) {
	
	$scope.hasUploadError = false;
    //Array in dem die hochgeladenen Files gehalten (und angezeigt) werden
    $scope.uploadedFiles = [];

    //Upload File Sucess
    $scope.uploadFileSuccess = function($file, $message, $flow) {

        $file.msg = " ";
        //Rückgabewert des Servers steht in: $message;
        //$scope.hasUploadError = true;
        //$scope.errorMessage = $file.name + " " + $message;
        //$scope.uploadedFiles.push($file);
    };

    $scope.uploadFileError = function($file, $message, $flow) {

        $file.err = true;
        $file.msg = $message;
        $scope.hasUploadError = true;

    };

    //Upload (Drop Area) Complete
    $scope.uploadComplete = function($flow,redirectUrl) {

		$window.location.href = redirectUrl;

        //$scope.hasUploadError = true;
        //$scope.errorMessage = "Alles hochgeladen";
        
    };

    $scope.uploadError = function($flow, $message, $file) {

        //$scope.hasUploadError = true;
        //$scope.errorMessage = $message;

    };
	
	
});

angular.module('ui.mediadesk').controller('ThumbnailViewCtrl', function ($scope, $http, $timeout, $window, $uibModal, ngDialog, toaster) {

    //$scope.periodeselected = 0;
	
	$scope.loadInProgress = 0; //0 = alles geladen/es wird nicht geladen | 1 = Noch eine Abfrage zu laden | 2 = noch zwei Abfragen zu laden

    $scope.apiUriPrefix = ""; //URL-Prefix für API aufrufe z.b. /api/rest/folder

    $scope.moreMosStep = 3*15; //Anzahl der Medienobjekte die bei mehr laden geholt werden (3 Spalten*15 Zeilen)
    $scope.selectedMedia = 0;

	$scope.basicView = "auto"; //Basic View beinhaltet die Einstellung des Containers. D.h. auch wenn list oder thumbnail gewählt wurde ist dieser Wert auto, wenn die im Container (z.B. Ordner) eingestellt Ansicht auto ist
    $scope.mosView = "auto"; //Ansicht: 0 =auto, 1 = thumbnails; 2 = list; Werte: list|thumbnails|auto
	

    //Hilfsfunktion zum auslesen des QueryStrings
    //Quelle http://stackoverflow.com/questions/979975/how-to-get-the-value-from-the-url-parameter
    var QueryString = function () {
      // This function is anonymous, is executed immediately and
      // the return value is assigned to QueryString!
      var query_string = {};
      var query = window.location.search.substring(1);
      var vars = query.split("&");
      for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
            // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
          query_string[pair[0]] = decodeURIComponent(pair[1]);
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
          var arr = [ query_string[pair[0]],decodeURIComponent(pair[1]) ];
          query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
          query_string[pair[0]].push(decodeURIComponent(pair[1]));
        }
      }
        return query_string;
    }();

    $scope.containerId = 0; //Standardmässig mit Root-Folder initialisieren

    if (QueryString.id != undefined) { //Wenn per QueryString eine Id übergeben wurde, dann diese nehmen
        //alert("init with folderid");
        //$scope.containerId = QueryString.id;
    } else {
        //alert("init with 0");
    }
	
	if ($window.location.hash.length>1) {
		
		$scope.showPreviewOnLoad = $window.location.hash.substring(2);
		console.log("$scope.showPreviewOnLoad = "+$scope.showPreviewOnLoad);
		
	}

    $scope.sortBy = 0;
    $scope.orderBy = 0;

    if (QueryString.sortBy != undefined)  { $scope.sortBy = QueryString.sortBy; };
    if (QueryString.orderBy != undefined) { $scope.orderBy = QueryString.orderBy; };


    //Medienobjekte in den Scope laden
    $scope.loadMos = function() {
		
	  $scope.loadInProgress = 1;
		
		//Medienobjekte laden
      $http.get($scope.apiUriPrefix+"/"+$scope.containerId+"/medialist?sortBy="+$scope.sortBy+"&orderBy="+$scope.orderBy)
        .then(function(response) {
          //alert('erfolgreich');
          $scope.allmos = response.data;
          $scope.mostail = $scope.moreMosStep;
          $scope.mos = $scope.allmos.slice(0,$scope.mostail);
		  $scope.loadInProgress = $scope.loadInProgress - 1; //dieser Teil geladen
		  if ($scope.loadInProgress==0) {
			$scope.mosView = $scope.getMosView();
		  }
          //alert('data: '+data);
		  
		  if ($scope.showPreviewOnLoad != undefined) {
			  //Preview anzeigen
			  console.log("Preview suchen...");
			  for (a=0;a<$scope.allmos.length;a++) {
				  console.log(a+" scope allmos ivid = "+$scope.allmos[a].ivid+" == "+$scope.showPreviewOnLoad);
				  if ($scope.allmos[a].ivid == $scope.showPreviewOnLoad) {
					  console.log("gefunden");
					  $scope.openPreview(a);
					  break;
				  }
			  }
		  }
		  //
		  
      }, function(response) {
		  $scope.loadInProgress = $scope.loadInProgress - 1; //dieser Teil geladen (wenn auch nicht korrekt)
          alert('Fehler: loadMos '+response);
      });
	  
	    //Unterordner laden
		console.log("$scope.apiUriPrefix="+$scope.apiUriPrefix);
		
	  if ($scope.apiUriPrefix=="/api/rest/folder") {
		console.log("Unterordner laden");
		$scope.loadInProgress = $scope.loadInProgress + 1;
		$http.get("/api/rest/folder/"+$scope.containerId+"/child/data.json")
		.then(function(response) {
		  //alert('erfolgreich');
			$scope.subfolders = response.data;
			$scope.loadInProgress = $scope.loadInProgress - 1; //dieser Teil geladen
			if ($scope.allmos != undefined) {
				$scope.mosView = $scope.getMosView();
			}
		  //$scope.groups = response.data;
		}, function(response) {
			$scope.loadInProgress = $scope.loadInProgress - 1; //dieser Teil geladen (wenn auch nicht korrekt)
			alert('Fehler: subfolder '+response);
		});
	  }
    }
	
	//Ermittelt den anzuzeigenden view, Wichtig wenn auto eingestellt oder gewählt ist
	
	$scope.getMosView = function() {
		
		if ($scope.basicView=="auto") {
						
			if ($scope.allmos.length==0) { 
				//Wenn keine Dateien dann Ordnerbilder prüfen: 
				var b = 0;
				
				if ($scope.apiUriPrefix=="/api/rest/folder") {
					for (b=0;b<$scope.subfolders.length;b++) {
						
						if ($scope.subfolders[b].pivid!=0) {
							//Mindestens ein Ordnerbild: thumbnails
							return "thumbnails";
						}
					}
				}
				//Kein Ordnerbild: Tabellen/Listenview
				return "list"; 
			} else {
				//Es gibt Dateien in diesem Container, wenn alle Bilder/Videos sind dann thumbnails ansonsten list
				var listView = false;
				var a = 0;
				
				for (a=0;a<$scope.mos.length;a++) {
					console.log("$scope.mos["+a+"].mayorMime = "+$scope.mos[a].mayorMime);
					
					if ($scope.mos[a].mayorMime == "image" || $scope.mos[a].mayorMime == "video") {
						//do nothing wenn video oder image
					} else {
						//Wenn kein Bild/Video vorkommt dann immer listView zeigen 
						listView = true;
					}
				}
				
				if (listView) {
					return "list";
				} else {
					return "thumbnails";
				}
			}
			
		} else {
			return $scope.basicView;
		}
	}
	

  //Select/Deselect
  $scope.onSelectChange = function(index) {
      //alert('selected'+index+"value="+$scope.mos[index].selected);
      ///api/rest/selectMedia/"+par1+"/"+val1+"/"+folderId+"/ajax=true/
      $http.get("/api/rest/selectMedia/"+$scope.mos[index].selected+"/"+$scope.mos[index].ivid+"/"+$scope.containerId+"/ajax=true/rand")
      .then(function(response) {
          $scope.selectedMedia= $scope.mos[index].selected ? $scope.selectedMedia+1 : $scope.selectedMedia-1;
      }, function(response) {
          $scope.mos[index].selected= !$scope.mos[index].selected
          alert("Fehler: onSelectChange "+response)
      });
  };

    //Mehr laden
    $scope.appendMos = function() {
        $scope.mostail = $scope.mostail+$scope.moreMosStep;
        $scope.mos = $scope.allmos.slice(0,$scope.mostail);
    }

    $scope.switchToView = function(view) {
		
		$scope.basicView = view;
        $scope.mosView = $scope.getMosView();
		
    }

    //Inititalisiere Ansicht via ng-init
    $scope.initMosView = function(apiUriPrefix, containerId, viewName, selectedMedia, sortBy, orderBy) {

        $scope.selectedMedia = selectedMedia;

		/*
        if (viewName=='listFiles.jsp') {
            $scope.mosView = 2;
        } else {
            $scope.mosView = 1;
        }
		*/
		$scope.basicView = viewName; //
		$scope.mosView = viewName;

        $scope.apiUriPrefix = apiUriPrefix;
        $scope.containerId = containerId;
		
		$scope.sortBy = sortBy;
		$scope.orderBy = orderBy;

        $scope.loadMos();
    }

    //Funktion alle medienobjekte auswählen
    $scope.selectMediaAll = function() {

        for (var a=0;a<$scope.mos.length;a++) {
            if (!$scope.mos[a].selected) {
                $scope.mos[a].selected = true;
                $scope.onSelectChange(a);
            }
        }
		
		toaster.pop('success', "Auswahl", "Alle Dateien ausgewählt");

    };

    //Funktion alle medienobjekte abwählen
    $scope.selectMediaNone = function() {

      ///api/rest/selectMedia/"+par1+"/"+val1+"/"+folderId+"/ajax=true/
      $http.get("/api/rest/selectMedia/false/")
      .then(function(response) {

            for (var a=0;a<$scope.mos.length;a++) {
                if ($scope.mos[a].selected) {
                    $scope.mos[a].selected = false;
                }
            }

          $scope.selectedMedia= 0;

      }, function(response) {
          alert("Fehler: selectMediaNone "+response)
      });

    };

    //removeMedia
    $scope.removeMedia = function() {

      $http.delete($scope.apiUriPrefix+"/"+$scope.containerId+"/removeselected")
      .then(function(response) {

          $scope.loadMos();
          $scope.selectedMedia= 0;
		  
		  toaster.pop('success', "Entfernen", "Ausgewählte Dateien von hier entfernt");

      }, function(response) {
		  
		  if (response.status == 403) {
			  toaster.pop('error', "Keine Berechtigung", "Berechtigung fehlt oder nicht eingeloggt");
		  } else {
			  toaster.pop('error', "Fehler", "Keine Berechtigung oder nicht eingeloggt status: "+response.status+", data: "+response.data);
		  }
		  
          //alert("Fehler: "+response)
      });

    };

    //insertMedia
    $scope.insertMedia = function() {

      $http.get($scope.apiUriPrefix+"/"+$scope.containerId+"/insertselected")
      .then(function(response) {

          $scope.loadMos();
          $scope.selectedMedia= 0;
		  
		  toaster.pop('success', "Einfügen", "Ausgewählte Dateien eingefügt");

      }, function(response) {
          alert("Fehler: inserMedia "+response)
      });

    }
	
	$scope.deleteMediaPopup = function() {

        var modalInstance = $uibModal.open({
          animation: true,
          size: 'sm',
          templateUrl: 'deleteMediaPopup.html',
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });	
		
	}

    //deleteMedia
    $scope.deleteMedia = function() {

      $http.delete($scope.apiUriPrefix+"/"+$scope.containerId+"/deleteselected")
      .then(function(response) {

          $scope.loadMos();
          $scope.selectedMedia= 0;
		  
		  toaster.pop('success', "Löschen", "Ausgewählte Dateien gelöscht");

      }, function(response) {
		  
		  if (response.status == 403) {
			  toaster.pop('error', "Keine Berechtigung", "Berechtigung fehlt oder nicht eingeloggt");
		  } else {
			  toaster.pop('error', "Fehler", "Keine Berechtigung oder nicht eingeloggt");
		  }
		  
          //alert("Fehler [deleteMedia]: "+response)
		  
      });

    };

    $scope.toCart = function(mo) {

		if (!mo.cart) {
			var ivid = mo.ivid;
			$http.put("/api/rest/cart/"+ivid)
			.then(function(response) {
				if ($scope.mo != undefined) {
					$scope.mo.cart = true;
				}
				//Bei den Medienobjekten in der Thumbnail/Tabellenliste auch cart auf true stellen
				for (var i=0;i<$scope.mos.length;i++) {
					if ($scope.mos[i].ivid == ivid) {
						$scope.mos[i].cart = true;
					}
				}
				$scope.properties.cartCount = $scope.properties.cartCount+1;
				
				toaster.pop('success', "Warenkorb", " wurde dem Warenkorb hinzugefügt");
				
			}, function(response) {
				alert("Fehler [toCart]: "+response);
			});
		} else {
			toaster.pop('error', "Warenkorb", "ist bereits im Warenkorb");
		}
    }

    $scope.toFav = function(mo) {
		
		if (!mo.fav) {
			var ivid = mo.ivid;
			$http.put("/api/rest/fav/"+ivid)
			.then(function(response) {
				if ($scope.mo != undefined) {
					$scope.mo.fav = true;
				}
				//Bei den Medienobjekten in der Thumbnail/Tabellenliste auch fav auf true stellen
				for (var i=0;i<$scope.mos.length;i++) {
					if ($scope.mos[i].ivid == ivid) {
						$scope.mos[i].fav = true;
					}
				}
				$scope.properties.favCount = $scope.properties.favCount+1;
				
				toaster.pop('success', "Favoriten", " wurde den Favoriten hinzugefügt");
			}, function(response) {
				alert("Fehler [toFav]: "+response);
			});
		
		} else {
			toaster.pop('error', "Favoriten", "ist bereits in den Favoriten");
		}
    }
	
	//Ausgewählte von den Favoriten entfernen
	$scope.deleteFromFav = function() {

		$http.delete("/api/rest/fav/deleteselected")
		.then(function(response) {

			//$scope.properties.favCount = $scope.properties.favCount+1;
			$scope.properties.favCount = $scope.properties.favCount-$scope.selectedMedia;
			$scope.selectedMedia = 0;
			$scope.loadMos();
			
		}, function(response) {
			alert("deletefromfav: "+response.status + " data: " + response.data);
		});
	
	}

    //Array in dem die hochgeladenen Files gehalten (und angezeigt) werden
    $scope.uploadedFiles = [];

    //Upload File Sucess
    $scope.uploadFileSuccess = function($file, $message, $flow) {

        $file.msg = " ";
        //Rückgabewert des Servers steht in: $message;
		toaster.pop('success', "OK", $file.name+" erfolgreich hochgeladen");
		
        //$scope.hasUploadError = true;
        //$scope.errorMessage = $file.name + " " + $message;
        //$scope.uploadedFiles.push($file);
    };

    $scope.uploadFileError = function($file, $message, $flow) {

        $file.err = true;
        $file.msg = $message;
        $scope.hasUploadError = true;
		
		//toaster.pop('error', "Fehler", $file.name+" konnte nicht hochgeladen werden: "+$file.msg);
		
		toaster.pop({ 
						type: 'error',
						title: "Fehler",
						body: $file.name+" konnte nicht hochgeladen werden: "+$file.msg,
						timeout: 0,
						showCloseButton: true
					});

    };

    //Upload (Drop Area) Complete
    $scope.uploadComplete = function($flow) {

        $scope.loadMos();

        if (!$scope.hasUploadError) { //Wenn es fehler gab, wird die Warteschlange nicht geleert
			if ($flow.files.length>1) {
				toaster.pop('success', "Upload", $flow.files.length+" Dateien hochgeladen");
			}
            $flow.files = [];
        } else {
			toaster.pop('error', "Upload Fehler", "Es gab Fehler beim Upload");
	
		}

        //$scope.hasUploadError = true;
        //$scope.errorMessage = "Alles hochgeladen";
        
    };

    $scope.uploadError = function($flow, $message, $file) {

        //$scope.hasUploadError = true;
        //$scope.errorMessage = $message;

    };

    //Open Preview (mediadetail/Vorschau)
    $scope.openPreview = function(index) {

        $scope.previewIndex = index;
        $scope.mo = $scope.mos[$scope.previewIndex];
        $scope.moloaded = false;
        //Alle Daten des Medienobjekts (Bild/Datei) laden:
          $http.get("/api/rest/mo/"+$scope.mo.ivid)
            .then(function(response) {
              //alert('erfolgreich');
			  var downloadlink = $scope.mo.downloadlink;
              $scope.mo = response.data;
			  $scope.mo.downloadlink = downloadlink; //Damit der richtige Downloadlink (PIN/Ordner) verfügbar ist
              $scope.moloaded = true;
          }, function(response) {
              alert('Fehler: openPreview '+response);
          });
		  
		  
		  //$scope.urlbeforemodal = $window.location.pathname+$window.location.search;
		  //var modalurl = $window.location.href+"#/ivid"+$scope.mo.ivid;
		  //console.log("url "+modalurl);
		  //$window.history.pushState(modalurl, "Title", modalurl);

        var modalInstance = $uibModal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'mediapreviewScript.html',
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });
		
		modalInstance.closed.then(function() {
			console.log("after modal open - close?"+ $window.location.pathname+$window.location.search);
			$window.history.pushState($scope.urlbeforemodal, "Title", $window.location.pathname+$window.location.search);
		});
		


        /* Nicht mehr verwendet weil nicht bootstrap kompatibel
        ngDialog.open({
            template: '/app/templates/mediapreview.html',
            className: 'ngdialog-theme-plain mediapreviewdialog',
            closeByEscape: true,
            closeByDocument: true,
            scope: $scope
        });
        */

    }
	
	//Prev und Next Funktion im Vorschaupopup
	$scope.prevPreview = function() {
		
		$scope.previewIndex = $scope.previewIndex-1;
		
        $scope.mo = $scope.mos[$scope.previewIndex];
        $scope.moloaded = false;
        //Alle Daten des Medienobjekts (Bild/Datei) laden:
          $http.get("/api/rest/mo/"+$scope.mo.ivid)
            .then(function(response) {
              //alert('erfolgreich');
              $scope.mo = response.data;
              $scope.moloaded = true;
			  
			  var previewHash = "#/"+$scope.mo.ivid;
			  $window.history.pushState($scope.urlbeforemodal, "Title", $window.location.pathname+$window.location.search+previewHash);
			  
          }, function(response) {
              alert('Fehler: prevPreview '+response);
          });
	}
	
	//Prev und Next Funktion im Vorschaupopup
	$scope.nextPreview = function() {
		
		$scope.previewIndex = $scope.previewIndex+1;
        $scope.mo = $scope.mos[$scope.previewIndex];
        $scope.moloaded = false;
        //Alle Daten des Medienobjekts (Bild/Datei) laden:
          $http.get("/api/rest/mo/"+$scope.mo.ivid)
            .then(function(response) {
              //alert('erfolgreich');
              $scope.mo = response.data;
			  if ($scope.mo.mayorMime!='image') {
				  //Bei image wird moloaded erst über das ImageOnLoad Tag auf true gestellt
				  $scope.moloaded = true;
			  }
			  
			  var previewHash = "#/"+$scope.mo.ivid;
			  $window.history.pushState($scope.urlbeforemodal, "Title", $window.location.pathname+$window.location.search+previewHash);
			  
          }, function(response) {
              alert('Fehler: nextPreview '+response);
          });
		
	}
	
	$scope.onPreviewImageLoaded = function() {
		console.log("onPreviewImageLoaded");
		$scope.moloaded = true;
	}

	$scope.openFancybox = function(index) {

        //Open Fancybox and load all images/media
		/*
	$scope.streamUrl = "/stream/object/"+$scope.mo.ivid+"."+$scope.mo.fileExtention;
	$scope.streamMimeType = $scope.mo.mime;

	$scope.posterUrl = "/imageservlet/"+$scope.mo.ivid+"/2/"+$scope.mo.name;
		 */
		var gallery = [];

        for (var a = 0; a < $scope.allmos.length; a++) {

        	var srcUrl = "/imageservlet/"+$scope.allmos[a].ivid+"/0/"+$scope.allmos[a].name;
        	if ($scope.allmos[a].mayorMime=='video') {
        		srcUrl = "/stream/object/"+$scope.allmos[a].ivid+"."+$scope.allmos[a].fileExtention;
			}

        	gallery[a] =             {
                src  : srcUrl,
                poster  : "/imageservlet/"+$scope.allmos[a].ivid+"/2/postergallery.jpg",
                opts : {
                    caption : $scope.allmos[a].title+"<br><small>"+$scope.allmos[a].subtitle+"</small> | <a href='"+$scope.allmos[a].downloadUrl+"'><i class='fa fa-download fa-fw md-icon-download'></a>",
                    thumb   : "/imageservlet/"+$scope.allmos[a].ivid+"/1/thumb.jpg",
                    poster  : "/imageservlet/"+$scope.allmos[a].ivid+"/2/poster.jpg"
                }
            };
        }

        $.fancybox.open(
        	gallery, {
            loop : false,
			video : {
            	/*
                tpl:
                '<video class="fancybox-video" controls controlsList="nodownload" poster="{{poster}}">' +
                '<source src="{{src}}" type="{{format}}" />' +
                'Sorry, your browser doesn\'t support embedded videos, <a href="{{src}}">download</a> and watch with your favorite video player!' +
                "</video>",*/
            	autoStart: false,
				preload: false
			}
        });

        $.fancybox.getInstance().jumpTo(index);

	}
	
    //Ausgewähltes Bild als Ordnerbild verwenden
    $scope.setFolderImage = function() {

        if ($scope.selectedMedia == 1) {


            $http.get($scope.apiUriPrefix + "/" + $scope.containerId + "/setfolderimageselected")
                .then(function (response) {
                    //alert('erfolgreich');
                    toaster.pop('success', "Ordnerbild", "Ordnerbild festgelegt");

                    //alle Medienobjekte abwählen
                    for (var a = 0; a < $scope.mos.length; a++) {
                        if ($scope.mos[a].selected) {
                            $scope.mos[a].selected = false;
                        }
                    }

                    $scope.selectedMedia = 0;

                }, function (response) {
                    toaster.pop('error', "Uuuups!", "Das hat nicht funktioniert");
                });

        } else if ($scope.selectedMedia==0) {
            toaster.pop('warning', "Geht nicht!", "Es ist kein Bild ausgewählt");
		} else {
            toaster.pop('warning', "Geht nicht!", "Es sind zu viele Bilder ausgewählt");
		}
	}

    //Drag&Drop
    $scope.dropSuccessHandler = function($event,index,mo){
		//Wenn ein Move (verschieben) beim Drag&Drop stattgefunden hat, muss das MO-Element gelöscht werden.
		console.log("dropSuccessHandler");
		if ($event.dataTransfer.dropEffect == 'move') {
			$scope.allmos.splice(index,1);
			$scope.mos.splice(index,1);
		}
    };
	
	//Wird von Flow-init aufgerufen https://github.com/flowjs/flow.js#configuration
	$scope.getUploadTarget = function() {
		if ($scope.apiUriPrefix=='/api/rest/folder') {
			console.log("get Upload Target: folder "+$scope.containerId);
			return '/gateway/upload/folder/'+$scope.containerId+'/';
		}
		if ($scope.apiUriPrefix=='/api/rest/pin') {
			console.log("get Upload Target: pin="+$scope.containerId);
			return '/gateway/upload/pin/'+$scope.containerId+'/?pinid='+$scope.containerId;
		}
	}

});

angular.module('ui.mediadesk').controller('CartViewCtrl', function ($scope, $http, $timeout, ngDialog) {

    $scope.loading = true;

    //Medienobjekte in den Scope laden
    $scope.loadMos = function() {
      $http.get("/api/rest/cart")
        .then(function(response) {
          //alert('erfolgreich');
          $scope.loading = false;
          
          $scope.allmos = response.data;
          $scope.mos = $scope.allmos
          //alert('data: '+data);
      }, function(response) {
          alert('Fehler beim laden des warenkorbs: '+response);
      });
    }

    $scope.loadMos();

    $scope.deleteFromCart = function(ivid) {

        $http.delete("/api/rest/cart/"+ivid)
        .then(function(response) {
			$scope.properties.cartCount = $scope.properties.cartCount-1;
            $scope.loadMos();
        }, function(response) {
            alert('Fehler beim löschen aus Warenkorb: '+response);
        });
    }

});


angular.module('ui.mediadesk').controller('AccordionDemoCtrl', function ($scope, $http) {
  $scope.oneAtATime = true;

  $http.get("/api/rest/folder/0/child2/data.json")
    .then(function(response) {
      //alert('erfolgreich');
      $scope.groups = response.data;
  }, function(response) {
      alert('Fehler: '+response);
  });

//  $scope.groups = [
//    {
//      title: 'Dynamic Group Header - 1',
//      items: [
//      {folder:'a', id:1, info:'nix'},
//      {folder:'bJesse', id:2, info:'nix'},
//      {folder:'vcY', id:3, info:'nix'}
//          ]
//    },
//    {
//      title: 'Dynamic Group Header - 2',
//      items: [
//      {folder:'John', id:1, info:'nix'},
//      {folder:'Jesse', id:2, info:'nix'},
//      {folder:'Y', id:3, info:'nix'}
//          ]
//    }
//  ];

  //$scope.items = ['Item 1', 'Item 2', 'Item 3'];

  $scope.addItem = function() {
    var newItemNo = $scope.items.length + 1;
    //$scope.items.push('Item ' + newItemNo);
    $scope.items.push({folder:'Item ' + newItemNo, id:newItemNo+1, info:'nix'});
  };

  $scope.status = {
    isFirstOpen: true,
    isFirstDisabled: false
  };
});

//NG Dialog
//http://www.initiative3n.ne/piwik/libs/angularjs/ngDialog/example/

angular.module('ui.mediadesk').controller('MainCtrl', function ($scope, $uibModal, $rootScope, $window, ngDialog) {

    $rootScope.jsonData = '{"foo": "bar"}';
    $rootScope.theme = 'ngdialog-theme-default';

        $scope.openTemplate = function () {
            $scope.value = true;


            ngDialog.open({
                //template: '/de/folderedit?parentCat=',
                template: '/test/externalTemplate.html',
                className: 'ngdialog-theme-plain',
                closeByEscape: false,
                closeByDocument: true,
                scope: $scope
            });
        }

        $scope.openFolderEdit = function (folderId) {
            $scope.value = true;

            ngDialog.open({
                template: '/de/folderedit?id='+folderId,
                className: 'ngdialog-theme-plain',
                closeByEscape: true,
                closeByDocument: true,
                scope: $scope
            });
        }

        $scope.openFolderCreate = function (parentId) {
            $scope.value = true;

            ngDialog.open({
                //template: '/de/folderedit?parentCat=',
                template: '/de/folderedit?parent='+parentId,
                className: 'ngdialog-theme-plain',
                closeByEscape: true,
                closeByDocument: true,
                scope: $scope
            });
        }

        $scope.openMediaEdit = function (ivId,redirectUrl) {
            //$scope.value = true;

            if (redirectUrl == undefined) { redirectUrl = $window.location; }
            $window.location.href = "mediadetailedit?id="+ivId+"&redirect="+redirectUrl;

        }
		
		$scope.openDownload = function (ivId) {
			
			$window.location.href = "/"+$scope.properties.lng+"/download?download=ivid&ivid="+ivId;
		}

    $scope.underConstruction = function() {

            ngDialog.open({
                template: '/app/templates/underconstruction.html',
                className: 'ngdialog-theme-plain',
                closeByEscape: true,
                closeByDocument: true,
                scope: $scope
            });

    }
	
	$scope.shareFb = function(url) {
		
		$window.open('https://www.facebook.com/sharer/sharer.php?u='+url, '_blank');
	}
	
	$scope.openContentPopup = function(url) {
		
        var modalInstance = $uibModal.open({
          animation: true,
          size: 'lg',
          templateUrl: url,
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });
		
	}
	
	$scope.openPinPopup = function(pin) {
		
		$scope.pinlink = pin;

        var modalInstance = $uibModal.open({
          animation: true,
          size: 'sm',
          templateUrl: 'pinPopup.html',
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });	
		
	}
	
	$scope.openOnePagerEditPopup = function(url) {
		
        var modalInstance = $uibModal.open({
          animation: true,
          size: 'lg',
          templateUrl: url,
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });
		
	}

});

//Bearbeiten-Controller z.b. fuer Ordner bearbeiten/erstellen, Beschlagwortung etc...

angular.module('ui.mediadesk').controller('EditCtrl', function ($scope, $uibModal, $rootScope, $window, ngDialog, $http) {
	
	$scope.showMore = false;
	$scope.showDe = true;
	$scope.showEn = true;
	$scope.syncNameTitle = true;
	$scope.syncDeEn = true;
	
	$scope.doShowMore = function() {
		
		$scope.showMore = true;		
	}
	
	//ALT: $scope.init = function(name, title1, title2) {
	//NEU: mit URL
	$scope.init = function(jsonApiUrl) {
		
		$http.get(jsonApiUrl)
		.then(function(response) {
			$scope.data = response.data;
			
			if ($scope.data.name==$scope.data.titleLng1 && $scope.data.name==$scope.data.titleLng2) {
				$scope.showMore = false;
				$scope.syncNameTitle = true;
				$scope.syncDeEn = true;
				$scope.showEn = false;
			} else {
				$scope.showMore = true;
				$scope.syncNameTitle = false;
				
				if ($scope.data.titleLng1==$scope.data.titleLng2) {
					$scope.syncDeEn = true;
					$scope.showEn = false;
				} else {
					$scope.syncDeEn = false;
				}
			}
			
		}, function(response) {
			alert('Fehler: '+response);
		});
		
	}
	
	$scope.nameChanged = function() {
		
		if ($scope.syncNameTitle) {
			$scope.data.titleLng1 = $scope.data.name;
			$scope.data.titleLng2 = $scope.data.name;
		}
	}
	
	$scope.title1Changed = function() {
		
		$scope.syncNameTitle = false;
		if ($scope.syncDeEn) {
			$scope.data.titleLng2 = $scope.data.titleLng1;
		}
	}

	$scope.title2Changed = function() {
		
		$scope.syncNameTitle = false;
		$scope.syncDeEn = false;
	}
	
	$scope.infoMediaPopup = function() {

        var modalInstance = $uibModal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'infoMediaPopup.html',
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });	
		
	}
	
	$scope.showDandE = function() {
		if ($scope.showEn && $scope.showDe) {
			return true;
		} else {
			return false;
		}
	}
	
	//TODO: Wird aktuell noch nicht verwendet (zukueftige releases!! mit Jackson!)
	$scope.submitForm = function() {
		
        $http({
			method  : 'POST',
			url     : '/api/rest/folder/',
			data    : $scope.data, //forms user object
			headers : {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'} 
        }).then(function(response) {

			var data = response.data;
			console.log('success post splashpage content');
            if (data.errors) {
              // Showing errors.
			  console.log('errors');
              $scope.errorWelcometext = data.errors.welcometext;
              $scope.errorSection1headline = data.errors.section1headline;
              $scope.errorSection1text = data.errors.section1text;
            } else {
				console.log('response message'+data.message);
              $scope.message = data.message;
			  location.reload(); 
            }
			
		}, function(response) {
			
			console.log('fehler post splashpage content');			
			toaster.pop('error', "Fehler", "fehler");
		});
    };	
	
});

//Video Videogular
angular.module('ui.mediadesk').controller('VideoCtrl',

["$sce", "$scope", function ($sce,$scope) {
	
	$scope.streamUrl = "/stream/object/"+$scope.mo.ivid+"."+$scope.mo.fileExtention;
	$scope.streamMimeType = $scope.mo.mime;
	
	$scope.posterUrl = "/imageservlet/"+$scope.mo.ivid+"/2/"+$scope.mo.name;
	
			this.config = {
				sources: [
				    {src: $sce.trustAsResourceUrl($scope.streamUrl), type: $scope.streamMimeType}
				],
				theme: "/app/lib/videogular-themes/default/videogular.css",
				plugins: {
					poster: $scope.posterUrl
				}
			};
		}]

);

//Statistik
angular.module('ui.mediadesk').controller('StatViewCtrl', function ($scope, $http, $timeout, ngDialog, $uibModal) {
		
		
    //Medienobjekte in den Scope laden
    $scope.loadStat = function(interval) {
		
	  $scope.interval = interval;
		
      $http.get("/api/rest/stat/interval/list/"+$scope.interval+"/1")
        .then(function(response) {
          //alert('erfolgreich');
          
          $scope.stat = response.data;
          //alert('data: '+data);
      }, function(response) {
          alert('Fehler beim laden der statistik: '+response);
      });
	  
	  //Chart
      $http.get("/api/rest/stat/interval/chart/"+$scope.interval+"/1")
        .then(function(response) {
          //alert('erfolgreich');
          
          $scope.data = response.data['data'];
		  $scope.labels = response.data['labels'];
		  $scope.series = response.data['series'];
          //alert('data: '+response.data['data']);
      }, function(response) {
          alert('Fehler beim laden der statistik: '+response);
      });
	  

    }
	
	//Stats Preview
    //Open Preview (mediadetail/Vorschau)
    $scope.openStatsPreview = function(statEntry) {

        $scope.mo = statEntry;
        $scope.moloaded = false;
        //Alle Daten des Medienobjekts (Bild/Datei) laden:
          $http.get("/api/rest/mo/"+statEntry.ivid)
            .then(function(response) {
              //alert('erfolgreich');
			  var downloadlink = $scope.mo.downloadlink;
              $scope.mo = response.data;
			  $scope.mo.downloadlink = downloadlink; //Damit der richtige Downloadlink (PIN/Ordner) verfügbar ist
          }, function(response) {
              alert('Fehler: openStatsPreview '+response);
          });
		  
		  
		  //$scope.urlbeforemodal = $window.location.pathname+$window.location.search;
		  //var modalurl = $window.location.href+"#/ivid"+$scope.mo.ivid;
		  //console.log("url "+modalurl);
		  //$window.history.pushState(modalurl, "Title", modalurl);

        var modalInstance = $uibModal.open({
          animation: true,
          size: 'lg',
          templateUrl: 'mediapreviewScript.html',
          //templateUrl: '/app/templates/mediapreview.html',
          scope : $scope
        });
		
		/*
		modalInstance.closed.then(function() {
			console.log("after modal open - close?"+ $window.location.pathname+$window.location.search);
			$window.history.pushState($scope.urlbeforemodal, "Title", $window.location.pathname+$window.location.search);
		});*/
		
	}
	
});

//Statistic Chart
angular.module('ui.mediadesk').controller("BarCtrl", function ($scope, $http) {
  $scope.labels = ['Jan', 'Feb', 'Mar', 'Apr', 'Mai', 'Jun', 'Jul','Aug','Sep','Okt','Nov','Dez'];
  //$scope.labels = ['Jan', 'Feb'];
  $scope.series = ['2015', '2016'];
  
      $http.get("/api/rest/stat/interval/chart/1/1")
        .then(function(response) {
          //alert('erfolgreich');
          
          $scope.data = response.data['data'];
		  $scope.labels = response.data['labels'];
		  $scope.series = response.data['series'];
          //alert('data: '+response.data['data']);
      }, function(response) {
          alert('Fehler beim laden der statistik: '+response);
      });

  //$scope.data = [
  //  [15, 59, 8, 81, 56, 25, 40, 65, 59, 86, 31, 56],
  //  [88, 48, 40, 19, 86, 27, 90, 65, 59, 80, 81, 56]
  //];
  
  //$scope.data = [
  //  [15, 59],
  //  [88, 48]
  //];
});

//Settings Controller (Programm Settings)
angular.module('ui.mediadesk').controller("SettingsCtrl", function ($scope, $http) {
	
	console.log("settings controller");
	
	$scope.shoppingCartSettingsChanged = function() {
		console.log("settings controller changed");
	}
	
	

});

//Directives
//focus="true" example <input type="text" focus="true">
// Common directive for Focus

angular.module('ui.mediadesk').directive('focus',

    function($timeout) {

        return {
            scope : {
                trigger : '@focus'
            },
            link : function(scope, element) {
                scope.$watch('trigger', function(value) {
                    if (value === "true") {
                        $timeout(function() {
                            element[0].focus();
                        });
                    }
                });
            }
        };
    }
);

//Start Drag&Drop
angular.module('ui.mediadesk').controller("DragDropCtrl2", function($scope) {
	
	alert('init drop');
	
	$scope.dropitem = "yxz";
	
	$scope.list = [];
	
    $scope.dropCallback = function(event, index, item, external, type, allowedType) {
		alert('dropped');
        $scope.logListEvent('dropped at', event, index, external, type);
        if (external) {
            if (allowedType === 'itemType' && !item.label) return false;
            if (allowedType === 'containerType' && !angular.isArray(item)) return false;
        }
        return item;
    };
});

angular.module('ui.mediadesk').controller("DragDropCtrl", function($scope) {
	
	$scope.helpervar = "xyz";
	
	$scope.handleDrop = function() {
		alert('Item has been dropped ');
		return false;
	}
});


//Directive Drag&Drop
angular.module('ui.mediadesk').directive('draggable', function() {
	
    return function(scope, element) {
        // this gives us the native JS object
        var el = element[0];

        el.draggable = true;

        el.addEventListener(
            'dragstart',
            function(e) {
                e.dataTransfer.effectAllowed = 'move';
                e.dataTransfer.setData('Text', this.id);
                this.classList.add('drag');
                //return false;
            }//,
            //false
        );

        el.addEventListener(
            'dragend',
            function(e) {
                this.classList.remove('drag');
                return false;
            },
            false
        );
    }
	
});

angular.module('ui.mediadesk').directive('droppable', function() {
    return {
        scope: {},
        link: function(scope, element) {
            // again we need the native object
            var el = element[0];
			
			el.addEventListener(
				'dragover',
				function(e) {
					e.dataTransfer.dropEffect = 'move';
					// allows us to drop
					if (e.preventDefault) e.preventDefault();
					this.classList.add('over');
					return false;
				},
				false
			);
			
			el.addEventListener(
				'dragenter',
				function(e) {
					this.classList.add('over');
					return false;
				},
				false
			);

			el.addEventListener(
				'dragleave',
				function(e) {
					this.classList.remove('over');
					return false;
				},
				false
			);

			el.addEventListener(
				'drop',
				function(e) {
					// Stops some browsers from redirecting.
					//if (e.stopPropagation) e.stopPropagation();
					
					alert('drop');
					
					//e.stopPropagation();

					this.classList.remove('over');

					var item = document.getElementById(e.dataTransfer.getData('Text'));
					this.appendChild(item);
					
					alert('drop2');
					
                    // call the drop passed drop function
                    scope.$apply('drop()');
					
					alert('drop3');

					e.preventDefault();
					return false;
				},
				true
			);




			
        }
    }
});

//Ende Drag&Drop

//ImageOnLoad Directive from http://stackoverflow.com/questions/26521106/is-there-a-callback-promise-for-when-images-load-when-using-ng-src

angular.module('ui.mediadesk').directive('imageonload', function () {
return {
    restrict: 'A',
    link: function (scope, element, attrs) {
        element.bind('load', function() {
            scope.$apply(attrs.imageonload);
        });
    }
};
});


//Mime-Icon Filter
//es wird der Mimetype (image/jpeg) übergeben und die CSS Classe für das Mime Icon wird zurückgegeben

angular.module('ui.mediadesk').filter('mimeIconClass', function() {
    return function(input) {

        var mimeTypeArray = input.split("/");
        if (mimeTypeArray.length>=2) {
			
            if (mimeTypeArray[0]=='image') {
                return "image";
            } else if (mimeTypeArray[0]=='video') {
				return "video";
			} else if (mimeTypeArray[0]=='audio') {
				return "audio";
			} else if (mimeTypeArray[0]=='text') {
				return "text";
			} else if (mimeTypeArray[1]=='msword') {
				return "word";
			} else if (mimeTypeArray[1]=='vnd.ms-excel') {
				return "excel";
			} else if (mimeTypeArray[1]=='vnd.ms-powerpoint') {
				return "powerpoint";
			} else if (mimeTypeArray[1]=='zip') {
				return "archive";
			} else if (mimeTypeArray[1]=='pdf') {
				return "pdf";
			} else {
				return " fa-file-o ";
			}
			
			//return mimeTypeArray[0]; //Hier wird der Haupt-MimeType zurückgegeben (z.b. image)
			
        } else {
            //Hier wird der gesamte Mimetype zurückgegeben z.b. wenn "word" der Mimetype ist
            return input;
        }
    };
});

//FileSize HumanReadable
angular.module('ui.mediadesk').filter('fileSizeH', function() {
    return function(input) {

        var fileSizeInBytes = input*1024; //input ist immer kb
        var i = -1;
        var byteUnits = [' kB', ' MB', ' GB', ' TB', 'PB', 'EB', 'ZB', 'YB'];
        do {
            fileSizeInBytes = fileSizeInBytes / 1024;
            i++;
        } while (fileSizeInBytes > 1024);

        return Math.max(fileSizeInBytes, 0.1).toFixed(1) + byteUnits[i];
    };
});

//Date Picker
angular.module('ui.mediadesk').controller('DatepickerCtrl', function ($scope) {
	
  $scope.inlineOptions = {
    customClass: getDayClass,
//    minDate: new Date(),
    showWeeks: true
  };

  $scope.dateOptions = {
//    dateDisabled: disabled,
    formatYear: 'yy',
    maxDate: new Date(2999, 5, 28),
    minDate: new Date(1000, 0,  1),
    startingDay: 1
  };

  // Disable weekend selection
  function disabled(data) {
    var date = data.date,
      mode = data.mode;
    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
  }

  $scope.toggleMin = function() {
    $scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
    $scope.dateOptions.minDate = $scope.inlineOptions.minDate;
  };

//  $scope.toggleMin();

  $scope.openDatepicker = function() {
    $scope.popupDatepicker.opened = true;
  };

  $scope.setDate = function(year, month, day) {
    $scope.dt = new Date(year, month-1, day);
  };

  $scope.popupDatepicker = {
    opened: false
  };

  var tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  var afterTomorrow = new Date();
  afterTomorrow.setDate(tomorrow.getDate() + 1);
  $scope.events = [
    {
      date: tomorrow,
      status: 'full'
    },
    {
      date: afterTomorrow,
      status: 'partially'
    }
  ];

  function getDayClass(data) {
    var date = data.date,
      mode = data.mode;
    if (mode === 'day') {
      var dayToCheck = new Date(date).setHours(0,0,0,0);

      for (var i = 0; i < $scope.events.length; i++) {
        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

        if (dayToCheck === currentDay) {
          return $scope.events[i].status;
        }
      }
    }

    return '';
  }
	
});

angular.module('ui.mediadesk').controller('TimeAgoCtrl', function ($scope) {
	
  $scope.setDate = function(timesmilli) {
    $scope.dt = new Date(timesmilli);
  }
  
});

//Splash Page Edit Controller
angular.module('ui.mediadesk').controller('splashPageEditController', function ($scope, $http) {
	
	$scope.content = {};
	
	//Content Laden:
        $http({
			method  : 'GET',
			url     : '/api/rest/splashpage/get.do'
        }).then(function(response) {

			var data = response.data;
			console.log('load splashpage content');
            if (data.errors) {
              // Showing errors.
			  console.log('errors');

            } else {
				console.log('load data message'+data.message);
              $scope.content = data;
            }
			
		}, function(response) {
			
			console.log('fehler get splashpage content');			

		});
	
	
	
	$scope.submitForm = function() {
		
        $http({
			method  : 'POST',
			url     : '/api/rest/splashpage/post.do',
			data    : $scope.content, //forms user object
			headers : {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'} 
        }).then(function(response) {

			var data = response.data;
			console.log('success post splashpage content');
            if (data.errors) {
              // Showing errors.
			  console.log('errors');
              $scope.errorWelcometext = data.errors.welcometext;
              $scope.errorSection1headline = data.errors.section1headline;
              $scope.errorSection1text = data.errors.section1text;
            } else {
				console.log('response message'+data.message);
              $scope.message = data.message;
			  location.reload(); 
            }
			
		}, function(response) {
			
			console.log('fehler post splashpage content');			
			toaster.pop('error', "Fehler", "fehler");
		});
    };	
	
});




