/*
 * Copyright (C) 2017 kkoudev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package frontier.device;

import java.util.UUID;



/**
 * UUID操作ユーティリティークラス。
 *
 * @author Kou
 *
 */
public final class UUIDHelper {


    /**
     * UUID : SDP
     */
    public static final UUID    PROTOCOL_SDP            =
        UUID.fromString("00000001-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UDP
     */
    public static final UUID    PROTOCOL_UDP            =
        UUID.fromString("00000002-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : RFCOMM
     */
    public static final UUID    PROTOCOL_RFCOMM         =
        UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : TCP
     */
    public static final UUID    PROTOCOL_TCP            =
        UUID.fromString("00000004-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : TCSBIN
     */
    public static final UUID    PROTOCOL_TCSBIN         =
        UUID.fromString("00000005-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : TCSAT
     */
    public static final UUID    PROTOCOL_TCSAT          =
        UUID.fromString("00000006-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : OBEX
     */
    public static final UUID    PROTOCOL_OBEX           =
        UUID.fromString("00000008-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : IP
     */
    public static final UUID    PROTOCOL_IP             =
        UUID.fromString("00000009-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : FTP
     */
    public static final UUID    PROTOCOL_FTP            =
        UUID.fromString("0000000A-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HTTP
     */
    public static final UUID    PROTOCOL_HTTP           =
        UUID.fromString("0000000C-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : WSP
     */
    public static final UUID    PROTOCOL_WSP            =
        UUID.fromString("0000000E-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : BNEP
     */
    public static final UUID    PROTOCOL_BNEP           =
        UUID.fromString("0000000F-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UPNP
     */
    public static final UUID    PROTOCOL_UPNP           =
        UUID.fromString("00000010-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HID
     */
    public static final UUID    PROTOCOL_HID            =
        UUID.fromString("00000011-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HCCC
     */
    public static final UUID    PROTOCOL_HCCC           =
        UUID.fromString("00000012-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HCDC
     */
    public static final UUID    PROTOCOL_HCDC           =
        UUID.fromString("00000014-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HN
     */
    public static final UUID    PROTOCOL_HN             =
        UUID.fromString("00000016-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AVCTP
     */
    public static final UUID    PROTOCOL_AVCTP          =
        UUID.fromString("00000017-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AVDTP
     */
    public static final UUID    PROTOCOL_AVDTP          =
        UUID.fromString("00000019-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : CMPT
     */
    public static final UUID    PROTOCOL_CMPT           =
        UUID.fromString("0000001B-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UDI_C_PLANE
     */
    public static final UUID    PROTOCOL_UDI_C_PLANE    =
        UUID.fromString("0000001D-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : L2CAP
     */
    public static final UUID    PROTOCOL_L2CAP          =
        UUID.fromString("00000100-0000-1000-8000-00805F9B34FB");


    /**
     * UUID : ServiceDiscoveryServer
     */
    public static final UUID    SERVICE_SERVICE_DISCOVERY_SERVER            =
        UUID.fromString("00001000-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : BrowseGroupDescriptor
     */
    public static final UUID    SERVICE_BROWSE_GROUP_DESCRIPTOR             =
        UUID.fromString("00001001-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : PublicBrowseGroup
     */
    public static final UUID    SERVICE_PUBLIC_BROWSE_GROUP                 =
        UUID.fromString("00001002-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : SerialPortService
     */
    public static final UUID    SERVICE_SERIAL_PORT                         =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : LANAccessUsingPPP
     */
    public static final UUID    SERVICE_LAN_ACCESS_USING_PPP                =
        UUID.fromString("00001102-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : DialupNetworking
     */
    public static final UUID    SERVICE_DIALUP_NETWORKING                   =
        UUID.fromString("00001103-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : IrMCSync
     */
    public static final UUID    SERVICE_IR_MC_SYNC                          =
        UUID.fromString("00001104-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : OBEXObjectPush
     */
    public static final UUID    SERVICE_OBEX_OBJECT_PUSH                    =
        UUID.fromString("00001105-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : OBEXFileTransfer
     */
    public static final UUID    SERVICE_OBEX_FILE_TRANSFER                  =
        UUID.fromString("00001106-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : IrMCSyncCommand
     */
    public static final UUID    SERVICE_IR_MC_SYNC_COMMAND                  =
        UUID.fromString("00001107-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : Headset
     */
    public static final UUID    SERVICE_HEADSET                             =
        UUID.fromString("00001108-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : CordlessTelephony
     */
    public static final UUID    SERVICE_CORDLESS_TELEPHONY                  =
        UUID.fromString("00001109-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AudioSource
     */
    public static final UUID    SERVICE_AUDIO_SOURCE                        =
        UUID.fromString("0000110A-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AudioSink
     */
    public static final UUID    SERVICE_AUDIO_SINK                          =
        UUID.fromString("0000110B-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AVRemoteControlTarget
     */
    public static final UUID    SERVICE_AV_REMOTE_CONTROL_TARGET            =
        UUID.fromString("0000110C-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AdvancedAudioDistribution
     */
    public static final UUID    SERVICE_ADVANCED_AUDIO_DISTRIBUTION         =
        UUID.fromString("0000110D-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AVRemoteControl
     */
    public static final UUID    SERVICE_AV_REMOTE_CONTROL                   =
        UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : VideoConferencing
     */
    public static final UUID    SERVICE_VIDEO_CONFERENCING                  =
        UUID.fromString("0000110F-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : Intercom
     */
    public static final UUID    SERVICE_INTERCOM                            =
        UUID.fromString("00001110-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : Fax
     */
    public static final UUID    SERVICE_FAX                                 =
        UUID.fromString("00001111-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HeadsetAudioGateway
     */
    public static final UUID    SERVICE_HEADSET_AUDIO_GATEWAY               =
        UUID.fromString("00001112-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : WAP
     */
    public static final UUID    SERVICE_WAP                                 =
        UUID.fromString("00001113-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : WAPClient
     */
    public static final UUID    SERVICE_WAP_CLIENT                          =
        UUID.fromString("00001114-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : PANU
     */
    public static final UUID    SERVICE_PANU                                =
        UUID.fromString("00001115-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : NAP
     */
    public static final UUID    SERVICE_NAP                                 =
        UUID.fromString("00001116-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : GN
     */
    public static final UUID    SERVICE_GN                                  =
        UUID.fromString("00001117-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : DirectPrinting
     */
    public static final UUID    SERVICE_DIRECT_PRINTING                     =
        UUID.fromString("00001118-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ReferencePrinting
     */
    public static final UUID    SERVICE_REFERENCE_PRINTING                  =
        UUID.fromString("00001119-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : Imaging
     */
    public static final UUID    SERVICE_IMAGING                             =
        UUID.fromString("0000111A-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ImagingResponder
     */
    public static final UUID    SERVICE_IMAGING_RESPONDER                   =
        UUID.fromString("0000111B-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ImagingAutomaticArchive
     */
    public static final UUID    SERVICE_IMAGING_AUTOMATIC_ARCHIVE           =
        UUID.fromString("0000111C-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ImagingReferenceObjects
     */
    public static final UUID    SERVICE_IMAGING_REFERENCE_OBJECTS           =
        UUID.fromString("0000111D-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : Handsfree
     */
    public static final UUID    SERVICE_HANDSFREE                           =
        UUID.fromString("0000111E-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HandsfreeAudioGateway
     */
    public static final UUID    SERVICE_HANDSFREE_AUDIO_GATEWAY             =
        UUID.fromString("0000111F-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : DirectPrintingReferenceObjects
     */
    public static final UUID    SERVICE_DIRECT_PRINTING_REFERENCE_OBJECTS   =
        UUID.fromString("00001120-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ReflectedUI
     */
    public static final UUID    SERVICE_REFLECTED_UI                        =
        UUID.fromString("00001121-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : BasicPringing
     */
    public static final UUID    SERVICE_BASIC_PRINGING                      =
        UUID.fromString("00001122-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : PrintingStatus
     */
    public static final UUID    SERVICE_PRINTING_STATUS                     =
        UUID.fromString("00001123-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HumanInterfaceDevice
     */
    public static final UUID    SERVICE_HUMAN_INTERFACE_DEVICE              =
        UUID.fromString("00001124-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HardcopyCableReplacement
     */
    public static final UUID    SERVICE_HARDCOPY_CABLE_REPLACEMENT          =
        UUID.fromString("00001125-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HCRPrint
     */
    public static final UUID    SERVICE_HCR_PRINT                           =
        UUID.fromString("00001126-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : HCRScan
     */
    public static final UUID    SERVICE_HCR_SCAN                            =
        UUID.fromString("00001127-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : CommonISDNAccess
     */
    public static final UUID    SERVICE_COMMON_ISDN_ACCESS                  =
        UUID.fromString("00001128-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : VideoConferencingGW
     */
    public static final UUID    SERVICE_VIDEO_CONFERENCING_GW               =
        UUID.fromString("00001129-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UDIMT
     */
    public static final UUID    SERVICE_UDIMT                               =
        UUID.fromString("0000112A-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UDITA
     */
    public static final UUID    SERVICE_UDITA                               =
        UUID.fromString("0000112B-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : AudioVideo
     */
    public static final UUID    SERVICE_AUDIO_VIDEO                         =
        UUID.fromString("0000112C-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : PnPInformation
     */
    public static final UUID    SERVICE_PNP_INFORMATION                     =
        UUID.fromString("00001200-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : GenericNetworking
     */
    public static final UUID    SERVICE_GENERIC_NETWORKING                  =
        UUID.fromString("00001201-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : GenericFileTransfer
     */
    public static final UUID    SERVICE_GENERIC_FILE_TRANSFER               =
        UUID.fromString("00001202-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : GenericAudio
     */
    public static final UUID    SERVICE_GENERIC_AUDIO                       =
        UUID.fromString("00001203-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : GenericTelephony
     */
    public static final UUID    SERVICE_GENERIC_TELEPHONY                   =
        UUID.fromString("00001204-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UPnP
     */
    public static final UUID    SERVICE_UPNP                                =
        UUID.fromString("00001205-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : UPnPIp
     */
    public static final UUID    SERVICE_UPNP_IP                             =
        UUID.fromString("00001206-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ESdpUPnPIpPan
     */
    public static final UUID    SERVICE_ESDP_UPNP_IP_PAN                    =
        UUID.fromString("00001300-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : ESdpUPnPIpLap
     */
    public static final UUID    SERVICE_ESDP_UPNP_IP_LAP                    =
        UUID.fromString("00001301-0000-1000-8000-00805F9B34FB");

    /**
     * UUID : EdpUPnpIpL2CAP
     */
    public static final UUID    SERVICE_EDP_UPNP_IP_L2CAP                   =
        UUID.fromString("00001302-0000-1000-8000-00805F9B34FB");



    /**
     * インスタンス生成防止。
     *
     */
    private UUIDHelper() {

        // 処理なし

    }


}
