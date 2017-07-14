/*
 * #%L
 * Trigger Sync Button - Share extension
 * %%
 * Copyright (C) 2015 - 2017 form4 GmbH & Co. KG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
(function()
{
	/**
	 * YUI Library aliases
	 */
	var Dom = YAHOO.util.Dom,
		Event = YAHOO.util.Event,
	    Selector = YAHOO.util.Selector;

   /**
    * ManualSyncTriggerButton constructor.
    * 
    * @param {String}
    *           htmlId The HTML id of the parent element
    * @return {Alfresco.ManualSyncTriggerButton} The new ManualSyncTriggerButton instance
    * @constructor
    */
   Alfresco.ManualSyncTriggerButton = function(htmlId)
   {
      Alfresco.ManualSyncTriggerButton.superclass.constructor.call(this, "Alfresco.ManualSyncTriggerButton", htmlId, []);

      return this;
   };
   
   YAHOO.extend(Alfresco.ManualSyncTriggerButton, Alfresco.component.Base,
   {
      /**
       * Object container for initialization options
       * 
       * @property options
       * @type object
       */
      options:
      {
      },

      /**
       * @method: onReady
       */
      onReady: function DocumentLinks_onReady()
      {
        Alfresco.util.createYUIButton(this, "manual-sync-button", this.onButtonClick);
      },

      onButtonClick: function onButtonClick()
      {
         Alfresco.util.Ajax.request(
         {
            method: "GET",
            url: Alfresco.constants.PROXY_URI + "form4/ldap/trigger/manualsync?siteId="+Alfresco.constants.SITE,
            successCallback:
            {
                fn: function form4_refreshSuccess(response) {
                	Alfresco.util.PopupManager.displayPrompt(
                            {
                               title: this.msg("form4.syncbutton.popup.title"),
                               text:  this.msg("form4.syncbutton.popup.text")
                            });
                },
                scope: this
            },
            failureCallback:
            {
               fn: function form4_refreshSuccess(response) {
                  Alfresco.util.PopupManager.displayPrompt(
                  {
                     title: this.msg("form4.syncbutton.popup.error.title"),
                     text:  this.msg("form4.syncbutton.popup.error.title")
                  });
               },
               scope: this
            }
         });
      }
   });
})();
