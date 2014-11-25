<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<ul ng-hide="!fundingSrvc.groups.length" class="workspace-fundings workspace-body-list bottom-margin-medium" ng-cloak>
    <li class="bottom-margin-small workspace-border-box card ng-scope" ng-repeat="group in fundingSrvc.groups | orderBy:sortState.predicate:sortState.reverse">
        <div class="work-list-container">
            <ul class="sources-edit-list">

                <!-- Header -->
                <li ng-show="editSources[group.groupId] == true" class="source-header" ng-class="{'source-active' : editSources[group.groupId] == true}" ng-model="group.activities">
                    <div class="sources-header">
                        <div class="row">
                            <div class="col-md-4 col-sm-4 col-xs-4">
                                Sources <span class="hide-sources" ng-click="editSources[group.groupId] = !editSources[group.groupId]">Close sources</span>
                            </div>
                            <div class="col-md-3 col-sm-3 col-xs-3">
                                Created
                            </div>
                            <div class="col-md-2 col-sm-3 col-xs-3">                                
                                Preferred
                            </div>
                            <div class="col-md-3 col-sm-2 col-xs-2 right">
                                <#if !(isPublicProfile??)>
                                    <div class="workspace-toolbar">
                                        <ul class="workspace-private-toolbar">
                                            <!--  old edit funding
                                            <li>
                                                <a ng-click="openEditFunding(group.getActive())" class="toolbar-button edit-item-button">
                                                    <span class="glyphicon glyphicon-pencil edit-option-toolbar" title=""></span>
                                                </a>
                                            </li>
                                             -->
                                             <li class="works-details" ng-show="editSources[group.groupId] == true">                                        
                                                <a ng-click="showDetailsMouseClick(group,$event);" ng-mouseenter="showTooltip(group.groupId+'-showHideDetails')" ng-mouseleave="hideTooltip(group.groupId+'-showHideDetails')">
                                                    <span ng-class="(moreInfo[group.groupId] == true) ? 'glyphicons collapse_top' : 'glyphicons expand'">
                                                    </span>
                                                </a>                                        
                                                <div class="popover popover-tooltip top show-hide-details-popover ng-hide" ng-show="showElement[group.groupId+'-showHideDetails'] == true">
                                                     <div class="arrow"></div>
                                                    <div class="popover-content">
                                                        <span ng-show="moreInfo[group.groupId] == false || moreInfo[group.groupId] == null" class="">Show Details</span>                                    
                                                        <span ng-show="moreInfo[group.groupId] == true" class="ng-hide">Hide Details</span>
                                                    </div>
                                                </div>                                        
                                            </li>
    
                                            <li>
                                                <@orcid.privacyToggle2  angularModel="group.getActive().visibility.visibility"
                                                    questionClick="toggleClickPrivacyHelp(group.getActive().putCode.value)"
                                                    clickedClassCheck="{'popover-help-container-show':privacyHelp[group.getActive().putCode.value]==true}"
                                                    publicClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PUBLIC', $event)"
                                                    limitedClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'LIMITED', $event)"
                                                    privateClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PRIVATE', $event)" />
                                            </li>
                                        </ul>
                                    </div>
                                </#if>
                             </div>

                        <!-- OLD BELOW  -->
                            <!--
                            <div class="col-md-9">
                               <span>
                                    <a ng-click="editSources[group.groupId] = false">
                                        <span class="glyphicon glyphicon-remove" ng-show="!bulkEditShow"></span> Hide additional sources
                                    </a>
                               </span>
                                <#if !(isPublicProfile??)>
                                    <span ng-show="editSources[group.groupId] == true">
                                        <a ng-click="deleteFundingConfirm(group.getActive().putCode.value, true)">
                                            <span class="glyphicon glyphicon-trash" ng-show="!bulkEditShow"></span> Delete all
                                        </a>
                                    </span>
                                </#if>
                            </div>
                            <#if !(isPublicProfile??)>
                                <div class="workspace-toolbar">
                                    <ul class="workspace-private-toolbar">
                                        <li class="">Validate with ng-show for works is userIsSource(work) || (group.hasKeys() && !group.hasUserVersion())
                                            <a ng-click="openEditFunding(group.getActive())" class="toolbar-button edit-item-button">
                                                <span class="glyphicon glyphicon-pencil edit-option-toolbar" title=""></span>
                                            </a>
                                        </li>
                                        <li>
                                            <@orcid.privacyToggle2  angularModel="group.getActive().visibility.visibility"
                                                questionClick="toggleClickPrivacyHelp(group.getActive().putCode.value)"
                                                clickedClassCheck="{'popover-help-container-show':privacyHelp[group.getActive().putCode.value]==true}"
                                                publicClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PUBLIC', $event)"
                                                limitedClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'LIMITED', $event)"
                                                privateClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PRIVATE', $event)" />
                                        </li>
                                    </ul>
                                </div>
                            </#if>

                             -->

                        </div>
                    </div>
                </li><!--  End of header -->


                <li ng-repeat="funding in group.activities" ng-show="group.activePutCode == funding.putCode.value || editSources[group.groupId] == true">
                    <!-- active row summary info -->
                    <div class="row" ng-show="group.activePutCode == funding.putCode.value">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <h3 class="workspace-title">
                                <h3 class="workspace-title">
                                <span ng-show="group.getActive().fundingTitle.title.value">{{group.getActive().fundingTitle.title.value}}:</span>
                                <span class="funding-name" ng-bind-html="group.getActive().fundingName.value"></span>
                            </h3>

                            <div class="info-detail">
                                <span class="funding-date" ng-show="group.getActive().startDate && !group.getActive().endDate">
                                    <span ng-show="group.getActive().startDate.year">{{group.getActive().startDate.year}}</span><span ng-show="group.getActive().startDate.month">-{{group.getActive().startDate.month}}</span>
                                    <#-- Do not move it to two lines -->
                                    <@orcid.msg 'workspace_fundings.dateSeparator'/> <@orcid.msg 'workspace_fundings.present'/>
                                    <#-- ########################### -->
                                </span>
                                <span class="funding-date" ng-show="group.getActive().startDate && group.getActive().endDate">
                                    <span ng-show="group.getActive().startDate.year">{{group.getActive().startDate.year}}</span><span ng-show="group.getActive().startDate.month">-{{group.getActive().startDate.month}}</span>
                                    <@orcid.msg 'workspace_fundings.dateSeparator'/>
                                    <span ng-show="group.getActive().endDate.year">{{group.getActive().endDate.year}}</span><span ng-show="group.getActive().endDate.month">-{{group.getActive().endDate.month}}</span>
                                </span>
                                <span class="funding-date" ng-show="!group.getActive().startDate && group.getActive().endDate">
                                     <span ng-show="group.getActive().endDate.year">{{group.getActive().endDate.year}}</span><span ng-show="group.getActive().endDate.month">-{{group.getActive().endDate.month}}</span>
                                </span>
                            </div>
                        </div>


                            <div class="col-md-3 workspace-toolbar">
                                <ul class="workspace-private-toolbar" ng-hide="editSources[group.groupId] == true">
                                    <#if !(isPublicProfile??)>
                                        <!-- Bulk edit tool / for future implementation
                                        <li ng-show="bulkEditShow == true" class="hidden-xs bulk-checkbox-item">                                
                                                <input type="checkbox" ng-model="bulkEditMap[funding.putCode.value]" class="bulk-edit-input ng-pristine ng-valid pull-right">                                                            
                                        </li>
                                        -->
                                    </#if>    
                                    <!--
                                    <li> Validate with ng-show for works is: !group.hasUserVersion() || userIsSource(work)
                                         <a href="" class="toolbar-button edit-item-button">
                                             <span class="glyphicon glyphicon-pencil edit-option-toolbar" title="" ng-click="openEditFunding(group.getActive())"></span>
                                         </a>
                                     </li>
                                      -->
                                      <!-- Show/Hide Details -->
                                    <li class="works-details" ng-hide="editSources[group.groupId] == true">                                        
                                        <a ng-click="showDetailsMouseClick(group,$event);" ng-mouseenter="showTooltip(group.groupId+'-showHideDetails')" ng-mouseleave="hideTooltip(group.groupId+'-showHideDetails')">
                                            <span ng-class="(moreInfo[group.groupId] == true) ? 'glyphicons collapse_top' : 'glyphicons expand'">
                                            </span>
                                        </a>                                        
                                        <div class="popover popover-tooltip top show-hide-details-popover" ng-show="showElement[group.groupId+'-showHideDetails'] == true">
                                             <div class="arrow"></div>
                                            <div class="popover-content">
                                                <span ng-show="moreInfo[group.groupId] == false || moreInfo[group.groupId] == null">Show Details</span>                                    
                                                <span ng-show="moreInfo[group.groupId] == true">Hide Details</span>
                                            </div>
                                        </div>                                        
                                    </li>
                                    <#if !(isPublicProfile??)>
                                        <li>
                                            <@orcid.privacyToggle2  angularModel="group.getActive().visibility.visibility"
                                                questionClick="toggleClickPrivacyHelp(group.getActive().putCode.value)"
                                                clickedClassCheck="{'popover-help-container-show':privacyHelp[group.getActive().putCode.value]==true}"
                                                publicClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PUBLIC', $event)"
                                                limitedClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'LIMITED', $event)"
                                                privateClick="fundingSrvc.setGroupPrivacy(group.getActive().putCode.value, 'PRIVATE', $event)" />
                                        </li>
                                    </#if>
                                </ul>
                            </div>

                    </div>

                    <!-- Active Row Identifiers / URL / Validations / Versions -->
                    <div class="row" ng-show="group.activePutCode == funding.putCode.value">
                         <div class="col-md-12 col-sm-12 bottomBuffer">
                             <ul class="id-details">
                                 <li>
                                     <span ng-repeat='ei in group.getActive().externalIdentifiers'>
                                        <span ng-bind-html='ei | externalIdentifierHtml:$first:$last:group.getActive().externalIdentifiers.length'>
                                        </span>
                                    </span>
                                 </li>
                             </ul>
                         </div>
                     </div>

                     <!-- more info -->
                     <#include "funding_more_info_inc_v3.ftl"/>

                     <!-- active row source display -->
                      <div class="row source-line" ng-show="group.activePutCode == funding.putCode.value">
                          <div class="col-md-4" ng-show="editSources[group.groupId] == true">
                              {{group.getActive().sourceName}}
                          </div>
                          <div class="col-md-3" ng-show="editSources[group.groupId] == true">
                              <div ng-show="editSources[group.groupId] == true"  ng-bind="funding.createdDate | ajaxFormDateToISO8601"></div>
                          </div>
                          <div class="col-md-3" ng-show="editSources[group.groupId] == true">
                          <span class="glyphicon glyphicon-check ng-hide" ng-show="funding.putCode.value == group.defaultPutCode"></span><span ng-show="funding.putCode.value == group.defaultPutCode"> Preferred source</span>
                             <#if !(isPublicProfile??)>
                                <div ng-show="editSources[group.groupId] == true">
                                    <a ng-click="fundingSrvc.makeDefault(group, funding.putCode.value);" ng-show="funding.putCode.value != group.defaultPutCode" class="">
                                         <span class="glyphicon glyphicon-unchecked"></span> Make Preferred
                                    </a>
                                </div>
                            </#if>
                          </div>
                          <div class="col-md-2 trash-source" ng-show="editSources[group.groupId] == true">

                            <#if !(isPublicProfile??)>
                                <ul class="sources-actions">
                                    <li>
                                        <a ng-click="openEditFunding(group.getActive())"  ng-click="openEditWork(group.getActive().putCode.value)" ng-mouseenter="showTooltip(group.groupId+'-editActiveSource')" ng-mouseleave="hideTooltip(group.groupId+'-editActiveSource')">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        <div class="popover popover-tooltip top edit-activeSource-popover" ng-show="showElement[group.groupId+'-editActiveSource'] == true">
                                            <div class="arrow"></div>
                                            <div class="popover-content">
                                                <span ng-hide="!userIsSource(work)">Edit my version</span>
                                                <span ng-show="!userIsSource(work)">Make a copy and edit</span>
                                            </div>
                                        </div>
                                    </li>
                                    <li>
                                        <a ng-click="deleteFundingConfirm(group.getActive().putCode.value, false)"  ng-mouseenter="showTooltip(group.groupId+'-deleteActiveSource')" ng-mouseleave="hideTooltip(group.groupId+'-deleteActiveSource')">
                                           <span class="glyphicon glyphicon-trash"></span>
                                        </a>

                                        <div class="popover popover-tooltip top delete-activeSource-popover" ng-show="showElement[group.groupId+'-deleteActiveSource'] == true">
                                             <div class="arrow"></div>
                                            <div class="popover-content">
                                                Delete this source                                
                                            </div>
                                        </div>

                                    </li>
                                </ul>
                            </#if>

                          </div>
                    </div>

                    <!-- not active row && edit sources -->
                    <div ng-show="group.activePutCode != funding.putCode.value" class="row source-line">
                        <div class="col-md-4 col-sm-4 col-xs-4">
                                <a ng-click="group.activePutCode = funding.putCode.value;">
                                {{group.getActive().sourceName}}
                            </a>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            {{group.getActive().lastModified | ajaxFormDateToISO8601}}
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-5">
                             <#if !(isPublicProfile??)>
                                <span class="glyphicon glyphicon-check" ng-show="funding.putCode.value == group.defaultPutCode"></span><span ng-show="funding.putCode.value == group.defaultPutCode"> Preferred source</span>
                                <a ng-click="fundingSrvc.makeDefault(group, funding.putCode.value); " ng-show="funding.putCode.value != group.defaultPutCode">
                                   <span class="glyphicon glyphicon-unchecked"></span> Make Preferred
                                </a>
                            </#if>
                        </div>


                        <div class="col-md-2 col-sm-2 col-xs-12 trash-source">
                            <#if !(isPublicProfile??)>
                                <ul class="sources-actions">
                                    <li> <!-- Validate with ng-show for works is: !group.hasUserVersion() || userIsSource(work) -->
                                        <a ng-click="openEditFunding(group.getActive())" ng-click="openEditWork(work.putCode.value)" ng-mouseenter="showTooltip(work.putCode.value+'-editInactiveSource')" ng-mouseleave="hideTooltip(work.putCode.value+'-editInactiveSource')">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>

                                        <div class="popover popover-tooltip top edit-inactiveSource-popover" ng-show="showElement[work.putCode.value+'-editInactiveSource'] == true">
                                            <div class="arrow"></div>
                                            <div class="popover-content">
                                                <span ng-hide="!userIsSource(work)">Edit my version</span>
                                                <span ng-show="!userIsSource(work)">Make a copy and edit</span>
                                            </div>
                                        </div>
                                    </li>
                                    <li>
                                        <a ng-click="deleteFundingConfirm(group.getActive().putCode.value, false)" ng-mouseenter="showTooltip(funding.putCode.value+'-deleteInactiveSource')" ng-mouseleave="hideTooltip(funding.putCode.value+'-deleteInactiveSource')">
                                            <span class="glyphicon glyphicon-trash"></span>
                                        </a>
                                        <div class="popover popover-tooltip top delete-inactiveSource-popover" ng-show="showElement[funding.putCode.value+'-deleteInactiveSource'] == true">
                                            <div class="arrow"></div>
                                            <div class="popover-content">
                                                Delete this source
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </#if>
                        </div>


                    </div>

                    <!-- Bottom row -->

                    <div class="row source-line" ng-hide="editSources[group.groupId] == true">
                        <div class="col-md-4">
                              SOURCE: {{funding.sourceName}}
                          </div>
                          <div class="col-md-3">                              
                              CREATED: <span ng-bind="funding.createdDate | ajaxFormDateToISO8601"></span>                              
                          </div>
                        <div class="col-md-3" ng-show="group.activePutCode == funding.putCode.value">
                            <span class="glyphicon glyphicon-check"></span><span> Preferred source</span> <span ng-hide="group.activitiesCount == 1">(</span><a ng-click="editSources[group.groupId] = !editSources[group.groupId]" ng-hide="group.activitiesCount == 1" ng-mouseenter="showTooltip(group.groupId+'-sources')" ng-mouseleave="hideTooltip(group.groupId+'-sources')">of {{group.activitiesCount}}</a><span ng-hide="group.activitiesCount == 1">)</span>
                            
                            <div class="popover popover-tooltip top sources-popover" ng-show="showElement[group.groupId+'-sources'] == true">
                                <div class="arrow"></div>
                                <div class="popover-content">
                                    Show other sources                                
                                </div>
                            </div>
                        </div>
                        <div class="col-md-2">
                            <ul class="sources-options" ng-cloak>
                                 <#if !(isPublicProfile??)>
                                <!--       
                                <li ng-show="userIsSource(funding) || (group.hasKeys() && !group.hasUserVersion())">
                                    <a ng-click="openEditFunding(group.getActive())" class="toolbar-button edit-item-button" ng-mouseenter="showTooltip(group.groupId+'-editSource')" ng-mouseleave="hideTooltip(group.groupId+'-editSource')">
                                        <span class="glyphicon glyphicon-pencil" ng-class="{'glyphicons git_create' : !userIsSource(funding)}" title=""></span>
                                    </a>
                                    <div class="popover popover-tooltip top edit-source-popover" ng-show="showElement[group.groupId+'-editSource'] == true">
                                    <div class="arrow"></div>
                                        <div class="popover-content">
                                            <span ng-hide="!userIsSource(funding)">Edit my version</span>
                                            <span ng-show="!userIsSource(funding)">Make a copy and edit</span>
                                        </div>
                                    </div>
                                </li>
                                -->
                                <li> <!-- Validate with ng-show for works is: !group.hasUserVersion() || userIsSource(work) -->
                                        <a ng-click="openEditFunding(group.getActive())" ng-mouseenter="showTooltip(group.groupId+'-editSource')" ng-mouseleave="hideTooltip(group.groupId+'-editSource')">
                                            <span class="glyphicon glyphicon-pencil"></span>
                                        </a>
                                        <div class="popover popover-tooltip top edit-source-popover" ng-show="showElement[group.groupId+'-editSource'] == true">
                                            <div class="arrow"></div>
                                            <div class="popover-content">
                                                <span ng-hide="!userIsSource(work)">Edit my version</span>
                                                <span ng-show="!userIsSource(work)">Make a copy and edit</span>
                                            </div>
                                        </div>
                                </li>
                                <li ng-hide="group.activitiesCount == 1 || editSources[group.groupId] == true">

                                    <a ng-click="editSources[group.groupId] = !editSources[group.groupId]" ng-mouseenter="showTooltip(group.groupId+'-deleteGroup')" ng-mouseleave="hideTooltip(group.groupId+'-deleteGroup')">
                                         <span class="glyphicon glyphicon-trash"></span>
                                    </a>
                                    <div class="popover popover-tooltip top delete-source-popover" ng-show="showElement[group.groupId+'-deleteGroup'] == true">
                                         <div class="arrow"></div>
                                        <div class="popover-content">
                                            Delete this source                                
                                        </div>
                                    </div>

                                </li>
                                   <li ng-show="group.activitiesCount == 1">
                                      <a ng-click="deleteFundingConfirm(group.getActive().putCode.value, false)" ng-mouseenter="showTooltip(group.groupId+'-deleteSource')" ng-mouseleave="hideTooltip(group.groupId+'-deleteSource')">
                                         <span class="glyphicon glyphicon-trash"></span>
                                     </a>

                                     <div class="popover popover-tooltip top delete-source-popover" ng-show="showElement[group.groupId+'-deleteSource'] == true">
                                             <div class="arrow"></div>
                                            <div class="popover-content">
                                                Delete this source                                
                                            </div>
                                    </div>
                                   </li>
                                </#if>
                            </ul>
                        </div>
                    </div>
                </li><!-- End line -->
               </ul>
         </div>
    </li>
</ul>

<div ng-show="fundingSrvc.loading == true;" class="text-center">
    <i class="glyphicon glyphicon-refresh spin x4 green" id="spinner"></i>
    <!--[if lt IE 8]>
        <img src="${staticCdn}/img/spin-big.gif" width="85" height ="85"/>
    <![endif]-->
</div>
<div ng-show="fundingSrvc.loading == false && fundingSrvc.groups.length == 0" class="" ng-cloak>
    <strong><#if (publicProfile)?? && publicProfile == true><@orcid.msg 'workspace_fundings_body_list.nograntaddedyet' /><#else><@orcid.msg 'workspace_fundings.havenotaddaffiliation' /><a ng-click="addFundingModal()"> <@orcid.msg 'workspace_fundings_body_list.addsomenow'/></a></#if></strong>
</div>

