<#--
 #%L
 Trigger Sync Button - Share extension
 %%
 Copyright (C) 2015 form4 GmbH & Co. KG
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->
<@markup id="form4-manual-sync-link-js" target="js" action="after">
	<@script type="text/javascript" src="${url.context}/res/components/invite/manualsynctrigger.js" group="invite"/>
</@>

<@markup id="form4-manual-sync-link-css" target="css" action="after">
	<@link href="${url.context}/res/components/invite/manualsynctrigger.css" group="invite"/>
</@>

<@markup id="form4-manual-sync-link-widgets" target="widgets" action="replace">
	<#assign el=args.htmlid?html>
	<script type="text/javascript">//<![CDATA[
		new Alfresco.ManualSyncTriggerButton("${el}").setOptions({}).setMessages(${messages});
	//]]></script>
</@>

<@markup id="form4-manual-sync-link" target="links" action="replace">
		<#assign el=args.htmlid?html>
		
         <div class="members-bar-links">
            <#list links as link>
               <a id="${el}-${link.id}" href="${link.href}" class="${link.cssClass!""}">${link.label?html}</a>
               <#if link_has_next>
                  <span class="separator">&nbsp;</span>
               </#if>
            </#list>

            <#if showSyncButton>
	            <span class="yui-button yui-push-button manual-sync" id="${args.htmlid?html}-manual-sync-button">
	               <span class="first-child"><button class="manual-sync-button">${msg("form4.syncbutton.label")}</button></span>
	            </span>
            </#if>
         </div>

</@>
