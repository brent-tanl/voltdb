// JavaScript Document

$( document ).ready(function() {

    var adminEditObjects = {
        //Edit Security objects
        btnEditSecurityOk: $("#btnEditSecurityOk"),
        btnEditSecurityCancel: $("#btnEditSecurityCancel"),
        LinkSecurityEdit: $("#securityEdit"),
        chkSecurity: $("#chkSecurity"),
        chkSecurityValue: $("#chkSecurity").is(":checked"),
        iconSecurityOption: $("#securityOptionIcon"),

        //Edit Auto Snapshot objects
        btnEditAutoSnapshotOk: $("#btnEditAutoSnapshotOk"),
        btnEditAutoSnapshotCancel: $("#btnEditAutoSnapshotCancel"),
        LinkAutoSnapshotEdit: $("#autoSnapshotEdit"),
        chkAutoSnapsot: $("#chkAutoSnapshot"),
        chkAutoSnapshotValue: $("#chkAutoSnapshot").is(":checked"),
        iconAutoSnapshotOption: $("#autoSnapshotIcon"),
        txtAutoSnapshot: $("#txtAutoSnapshot"),
        //Frequency objects
        tBoxAutoSnapshotFreq: $("#txtFrequency"),
        tBoxAutoSnapshotFreqValue: $("#frequencySpan").text(),
        spanAutoSnapshotFreq: $("#frequencySpan"),
        //Retained objects
        tBoxAutoSnapshotRetained: $("#txtRetained"),
        tBoxAutoSnapshotRetainedValue: $("#retainedSpan").text(),
        spanAutoSnapshotRetained: $("#retainedSpan"),
        
    };

    $(".tblshutdown").find(".edit").on("click", function() {
        var $this = $(this).closest("tr");

        var tdVal = $this.find("td:nth-child(3)");
        var val = tdVal.text();

        if (val == "On") {
            $this.find("td:nth-child(2)").find("div").removeClass("onIcon").addClass("offIcon");
            tdVal.text("Off");
        } else {
            $this.find("td:nth-child(2)").find("div").removeClass("offIcon").addClass("onIcon");
            tdVal.text("On");
        }
    });
	
	
   // Make Expandable Rows.
    $('tr.parent > td:first-child' || 'tr.parent > td:fourth-child')
        .css("cursor", "pointer")
        .attr("title", "Click to expand/collapse")
        .click(function() {
            var parent = $(this).parent();
            parent.siblings('.child-' + parent.attr("id")).toggle();
            parent.find(".labelCollapsed").toggleClass("labelExpanded");
        });
   $('tr[class^=child-]').hide().children('td');
	
	// btnServerConfigAdmin
	$('#btnServerConfigAdmin').click(function(){
			$('#serverConfigAdmin').slideToggle("slide");
		});
		
		$('#serverName').click(function(){
			$('#serverConfigAdmin').slideToggle("slide");
		});
		
	// Implements Scroll in Server List div
		$('#serverListWrapperAdmin').slimscroll({
		  disableFadeOut: true,
		  height:'225px'	  
		});
		
		$('#shutDownConfirmation').popup();

		$('#pauseConfirmation').popup({
		    open: function (event, ui, ele) {
		    },
		    afterOpen: function() {

		        $("#btnPauseConfirmationOk").unbind("click");
		        $("#btnPauseConfirmationOk").on("click", function() {

		            $("#pauseConfirmation").hide();
		            $("#resumeConfirmation").show();

		            //Close the popup
		            $($(this).siblings()[0]).trigger("click");
		        });
		    }
		});

        $('#resumeConfirmation').popup({
            open: function(event, ui, ele) {
            },
            afterOpen: function() {

                $("#btnResumeConfirmationOk").unbind("click");
                $("#btnResumeConfirmationOk").on("click", function() {

                    $("#resumeConfirmation").hide();
                    $("#pauseConfirmation").show();

                    //Close the popup
                    $($(this).siblings()[0]).trigger("click");
                });
            }
        });
    
        var toggleSecurityEdit = function (showEdit) {

            if (adminEditObjects.chkSecurityValue) {
                adminEditObjects.chkSecurity.iCheck('check');
            } else {
                adminEditObjects.chkSecurity.iCheck('uncheck');
            }
            
            if (showEdit) {
                adminEditObjects.chkSecurity.parent().removeClass("customCheckbox");
                adminEditObjects.btnEditSecurityOk.hide();
                adminEditObjects.btnEditSecurityCancel.hide();
                adminEditObjects.LinkSecurityEdit.show();
                adminEditObjects.iconSecurityOption.show();
            } else {
                adminEditObjects.iconSecurityOption.hide();
                adminEditObjects.LinkSecurityEdit.hide();
                adminEditObjects.btnEditSecurityOk.show();
                adminEditObjects.btnEditSecurityCancel.show();
                adminEditObjects.chkSecurity.parent().addClass("customCheckbox");
            }
        };

        adminEditObjects.LinkSecurityEdit.on("click", function () {
            toggleSecurityEdit(false);
        });
    
        adminEditObjects.btnEditSecurityCancel.on("click", function () {
            toggleSecurityEdit(true);
        });
		
        adminEditObjects.btnEditSecurityOk.popup({
		     open: function (event, ui, ele) {
		     },
		     afterOpen: function () {

		         $("#btnSecurityOk").unbind("click");
		         $("#btnSecurityOk").on("click", function() {

		             var securityRow = $("tr.security");
		             var tdVal = securityRow.find("td:nth-child(3)");

		             if (adminEditObjects.chkSecurity.is(':checked')) {
		                 tdVal.text("On");
		                 adminEditObjects.iconSecurityOption.removeClass().addClass("onIcon");
		                 adminEditObjects.chkSecurityValue = true;
		             } else {
		                 tdVal.text("Off");
		                 adminEditObjects.iconSecurityOption.removeClass().addClass("offIcon");
		                 adminEditObjects.chkSecurityValue = false;
		             }
		             
		             //Close the popup
		             $($(this).siblings()[0]).trigger("click");
		         });
		         
		         $("#btnPopupSecurityCancel").on("click", function () {
		             toggleSecurityEdit(true);
		         });

		         $(".popup_back").on("click", function () {
		             toggleSecurityEdit(true);
		         });
		         
		         $(".popup_close").on("click", function () {
		             toggleSecurityEdit(true);
		         });
		     }
		 });
        
    
		 $('#saveConfirmation').popup();
		 $('#restoreConfirmation').popup();
		 $('#btnSaveHrtTimeOut').popup();
		 $('#btnSaveQueryTimeOut').popup();
		 $('#shutdownPopupPopConfirmation').popup();
		  $('#shutdownPopupPopConfirmation1').popup();
		  $('#shutdownClusterPopupPopConfirmation').popup();
		 $('#stopConfirmation').popup({
		    open: function (event, ui, ele) {
		    },
		    afterOpen: function() {

		        $("#StoptConfirmOK").unbind("click");
		        $("#StoptConfirmOK").on("click", function() {

		            $("#stopConfirmation").hide();
		            $("#startConfirmation").show();

		            //Close the popup
		            $($(this).siblings()[0]).trigger("click");
		        });
		    }
		});
		
		
		$('#startConfirmation').popup({
            open: function(event, ui, ele) {
            },
            afterOpen: function() {

                $("#startConfirmOk").unbind("click");
                $("#startConfirmOk").on("click", function() {

                    $("#startConfirmation").hide();
                    $("#stopConfirmation").show();

                    //Close the popup
                    $($(this).siblings()[0]).trigger("click");
                });
            }
        });
		 // $('.saveConfirmation').popup();
		 // $('.restoreConfirmation').popup();


    var toggleAutoSnapshotEdit = function(showEdit) {

        if (adminEditObjects.chkAutoSnapshotValue) {
            adminEditObjects.chkAutoSnapsot.iCheck('check');
        } else {
            adminEditObjects.chkAutoSnapsot.iCheck('uncheck');
        }
        
        adminEditObjects.tBoxAutoSnapshotFreq.val(adminEditObjects.tBoxAutoSnapshotFreqValue);
        adminEditObjects.tBoxAutoSnapshotRetained.val(adminEditObjects.tBoxAutoSnapshotRetainedValue);

        if (showEdit) {
            adminEditObjects.chkAutoSnapsot.parent().removeClass("customCheckbox");
            adminEditObjects.btnEditAutoSnapshotOk.hide();
            adminEditObjects.btnEditAutoSnapshotCancel.hide();
            adminEditObjects.LinkAutoSnapshotEdit.show();
            adminEditObjects.iconAutoSnapshotOption.show();

            adminEditObjects.tBoxAutoSnapshotFreq.hide();
            adminEditObjects.tBoxAutoSnapshotRetained.hide();
            adminEditObjects.spanAutoSnapshotFreq.show();
            adminEditObjects.spanAutoSnapshotRetained.show();
        } else {
            adminEditObjects.iconAutoSnapshotOption.hide();
            adminEditObjects.LinkAutoSnapshotEdit.hide();
            adminEditObjects.btnEditAutoSnapshotOk.show();
            adminEditObjects.btnEditAutoSnapshotCancel.show();
            adminEditObjects.chkAutoSnapsot.parent().addClass("customCheckbox");
            
            adminEditObjects.spanAutoSnapshotFreq.hide();
            adminEditObjects.spanAutoSnapshotRetained.hide();
            adminEditObjects.tBoxAutoSnapshotFreq.show();
            adminEditObjects.tBoxAutoSnapshotRetained.show();
        }
    };

    adminEditObjects.btnEditAutoSnapshotCancel.on("click", function () {
        toggleAutoSnapshotEdit(true);
    });

    adminEditObjects.btnEditAutoSnapshotOk.popup({
        open: function (event, ui, ele) {
        },
        afterOpen: function () {

            $("#btnSaveSnapshot").unbind("click");
            $("#btnSaveSnapshot").on("click", function () {

                if (adminEditObjects.chkAutoSnapsot.is(':checked')) {
                    adminEditObjects.txtAutoSnapshot.text("On");
                    adminEditObjects.iconAutoSnapshotOption.removeClass().addClass("onIcon");
                    adminEditObjects.chkAutoSnapshotValue = true;
                } else {
                    adminEditObjects.txtAutoSnapshot.text("Off");
                    adminEditObjects.iconAutoSnapshotOption.removeClass().addClass("offIcon");
                    adminEditObjects.chkAutoSnapshotValue = false;
                }
                
                adminEditObjects.tBoxAutoSnapshotFreqValue = adminEditObjects.tBoxAutoSnapshotFreq.val();
                adminEditObjects.tBoxAutoSnapshotRetainedValue = adminEditObjects.tBoxAutoSnapshotRetained.val();
                
                adminEditObjects.spanAutoSnapshotFreq.html(adminEditObjects.tBoxAutoSnapshotFreqValue);
                adminEditObjects.spanAutoSnapshotRetained.html(adminEditObjects.tBoxAutoSnapshotRetainedValue);

                //Close the popup
                $($(this).siblings()[0]).trigger("click");
            });

            $("#btnPopupAutoSnapshotCancel").on("click", function () {
                toggleAutoSnapshotEdit(true);
            });

            $(".popup_back").on("click", function () {
                toggleAutoSnapshotEdit(true);
            });

            $(".popup_close").on("click", function () {
                toggleAutoSnapshotEdit(true);
            });
        }
    });

    adminEditObjects.LinkAutoSnapshotEdit.click(function () {
        var parent = $(this).parent().parent();
        parent.siblings('.child-' + parent.attr("id")).show();
        parent.find(".labelCollapsed").addClass("labelExpanded");
        toggleAutoSnapshotEdit(false);
    });

		 //Heartbeat time out
		 	
		$('#btnEditHrtTimeOut').click(function(e){
			if(  $("#hrtTimeOutSpan").is(":visible") == true ){
				$('#btnEditHrtTimeOut').hide();
				$('#btnSaveHrtTimeOut').show();
				$("#hrtTimeOutSpan").hide();
				$("#txtHrtTimeOutSpan").show();
				$("#txtHrtTimeOutSpan").val($("#hrtTimeOutSpan").html())
			}else{
				$("#hrtTimeOutSpan").show();
				$("#txtHrtTimeOutSpan").hide();
				$("#hrtTimeOutSpan").html($("#txtHrtTimeOutSpan").val())
			}
			$("td.heartbeattd span").toggleClass("unit");
			e.preventDefault();
		});
		
		$('#btnSaveHrtTimeOut').click(function(e){
			$("#hrtTimeOutSpan").show();
			$("#txtHrtTimeOutSpan").hide();
			$('#btnEditHrtTimeOut').show();
			$('#btnSaveHrtTimeOut').hide();
			$("#hrtTimeOutSpan").html($("#txtHrtTimeOutSpan").val())
			$("td.heartbeattd span").toggleClass("unit");
		});
		
		
		
		 //Query time out
		 	
		$('#btnEditQueryTimeOut').click(function(e){
			if(  $("#queryTimeOutSpan").is(":visible") == true ){
				$('#btnEditQueryTimeOut').hide();
				$('#btnSaveQueryTimeOut').show();
				$("#queryTimeOutSpan").hide();
				$("#txtQryTimeOutSpan").show();
				$("#txtQryTimeOutSpan").val($("#queryTimeOutSpan").html())
			}else{
				$("#queryTimeOutSpan").show();
				$("#txtQryTimeOutSpan").hide();
				$("#queryTimeOutSpan").html($("#txtQryTimeOutSpan").val())
			}
			$("td.queryTimeOut span").toggleClass("unit");
			e.preventDefault();
		});
		
		$('#btnSaveQueryTimeOut').click(function(e){
			$("#queryTimeOutSpan").show();
			$("#txtQryTimeOutSpan").hide();
			$('#btnEditQueryTimeOut').show();
			$('#btnSaveQueryTimeOut').hide();
			$("#queryTimeOutSpan").html($("#txtQryTimeOutSpan").val())
			$("td.queryTimeOut span").toggleClass("unit");
		});
		
	 // Filters servers list
    $('#popServerSearchAdmin').keyup(function () {
        var that = this;
        $.each($('.tblshutdown tbody tr'),
        function (i, val) {
            if ($(val).text().indexOf($(that).val().toLowerCase()) == -1) {
                $('.tblshutdown tbody tr').eq(i).hide();
            } else {
                $('.tblshutdown tbody tr').eq(i).show();
            }
        });
    });	
	
	// Hides opened serverlist
		$(document).on('click', function (e) {
			if ( !$(event.target).hasClass('adminIcons') && !$(event.target).hasClass('serverName')) {
				if ($(e.target).closest("#serverConfigAdmin").length === 0) {
					$("#serverConfigAdmin").hide();
				}
			}
		});
		
		
		//server icon
		/*$('.adminServer').click(function(){			
					$("#serverConfigAdmin").toggle();			
		}); 
		$(document).mouseup(function (e)
			{
			var container = $("#serverConfigAdmin");
			
			if (!container.is(e.target) // if the target of the click isn't the container...
			&& container.has(e.target).length === 0) // ... nor a descendant of the container
			{
			container.hide();
			}
		});
		
		*/
		
	// Checkbox style
	
	
	  $('input.snapshot').iCheck({
		checkboxClass: 'icheckbox_square-aero',
		increaseArea: '20%' // optional
	
		});

	  $('#chkSecurity').iCheck({
	      checkboxClass: 'icheckbox_square-aero',
	      increaseArea: '20%' // optional
	  });
});

