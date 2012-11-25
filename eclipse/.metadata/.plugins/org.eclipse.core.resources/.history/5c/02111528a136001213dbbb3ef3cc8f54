/*
***************************************************************************
* COPYRIGHT (c) 1997 by Industrial Peer-to-Peer, Inc. All Rights Reserved *
*                                                                         *
* This program is an unpublished copyrighted work which is proprietary    *
* to Industrial Peer-to-Peer, Inc. and contains confidential information  *
* that is not to be reproduced or disclosed, in whole or in part, to any  *
* other person or entity without prior written consent from Industrial    *
* Peer-to-Peer, Inc. in each and every instance.                          *
*                                                                         *
* WARNING:  Unauthorized reproduction of this program as well as          *
* unauthorized preparation of derivative works based upon the             *
* program or distribution of copies by sale, rental, lease or             *
* lending are violations of federal copyright laws and state trade        *
* secret laws, punishable by civil and criminal penalties.                *
*                                                                         *
***************************************************************************
*/
// ezdnaapi.h : main header file for the EZDNAAPI DLL
//

#define EZDNA_FLOAT_ERROR -1.7E308


int WINAPI DNAGoodPointFormat(char *pszPoint);


/***************************/
/* real-time service calls */
/***************************/

int WINAPI DNAGetRTAll( char *szPoint,
                        double *pdValue,
                        char *szTime,      unsigned short nTime,
                        char *szStatus,    unsigned short nStatus,
                        char *szDesc,      unsigned short nDesc,
                        char *szUnits,     unsigned short nUnits
                      );
int WINAPI DNAGetRTAllUTC( char *szPoint,
                           double *pdValue,
                           long *ptTime,
                           char *szStatus,    unsigned short nStatus,
                           char *szDesc,      unsigned short nDesc,
                           char *szUnits,     unsigned short nUnits
                         );
int WINAPI DNAGetRTValue(char *szPoint, double *pdValue);
int WINAPI DNAGetRTTime(char *szPoint, char *szTime, unsigned short nTime);
int WINAPI DNAGetRTTimeUTC(char *szPoint, long *ptTime);
int WINAPI DNAGetRTStatus(char *szPoint, char *szStatus, unsigned short nStatus);
int WINAPI DNAGetRTDesc(char *szPoint, char *szDesc, unsigned short nDesc);
int WINAPI DNAGetRTUnits(char *szPoint, char *szUnits, unsigned short nUnits);


/* note: the list calls are unavailabe in Visual Basic */
int WINAPI DNAGetRTAllList( unsigned short nCount,
                            char *pszPoint[],
                            double pdValue[],
                            char *pszTime[],   unsigned short nTime,
                            char *pszStatus[], unsigned short nStatus,
                            char *pszDesc[],   unsigned short nDesc,
                            char *pszUnits[],  unsigned short nUnits
                          );
int WINAPI DNAGetRTAllListUTC( unsigned short nCount,
                               char *pszPoint[],
                               double pdValue[],
                               long ptTime[],
                               char *pszStatus[], unsigned short nStatus,
                               char *pszDesc[],   unsigned short nDesc,
                               char *pszUnits[],  unsigned short nUnits
                             );
int WINAPI DNAGetRTValueList(unsigned short nCount, char *pszPoint[], double pdValue[]);
int WINAPI DNAGetRTTimeList( unsigned short nCount,
                             char *pszPoint[],
                             char *pszTime[], unsigned short nTime
                           );
int WINAPI DNAGetRTTimeListUTC(unsigned short nCount, char *pszPoint[], long ptTime[]);
int WINAPI DNAGetRTStatusList( unsigned short nCount,
                               char *pszPoint[],
                               char *pszStatus[], unsigned short nStatus
                             );
int WINAPI DNAGetRTDescList( unsigned short nCount,
                             char *pszPoint[],
                             char *pszDesc[], unsigned short nDesc
                           );
int WINAPI DNAGetRTUnitsList( unsigned short nCount,
                              char *pszPoint[],
                              char *pszUnits[], unsigned short nUnits
                            );


/***************************/
/* history service calls   */
/***************************/

int WINAPI DnaGetHistAvgUTC(char *szPoint,
                            long tStart,
                            long tEnd,
                            long tPeriod,
                            unsigned long *pulKey
                           );

int WINAPI DnaGetHistAvg( char *szPoint,
                          char *szStart,
                          char *szEnd,
                          char *szPeriod,
                          unsigned long *pulKey
                        );
int WINAPI DnaGetHistSnapUTC( char *szPoint,
                              long tStart,
                              long tEnd,
                              long tPeriod,
                              unsigned long *pulKey
                            );
int WINAPI DnaGetHistSnap( char *szPoint,
                           char *szStart,
                           char *szEnd,
                           char *szPeriod,
                           unsigned long *pulKey
                         );

int WINAPI DnaGetHistMaxUTC( char *szPoint,
                             long tStart,
                             long tEnd,
                             long tPeriod,
                             unsigned long *pulKey
                           );
int WINAPI DnaGetHistMax( char *szPoint,
                          char *szStart,
                          char *szEnd,
                          char *szPeriod,
                          unsigned long *pulKey
                        );
int WINAPI DnaGetHistMinUTC( char *szPoint,
                             long tStart,
                             long tEnd,
                             long tPeriod,
                             unsigned long *pulKey
                           );
int WINAPI DnaGetHistMin( char *szPoint,
                          char *szStart,
                          char *szEnd,
                          char *szPeriod,
                          unsigned long *pulKey
                        );
int WINAPI DnaGetNextHistUTC( unsigned long ulKey,
                              double *pdValue,
                              long *ptTime,
                              char *szStatus, unsigned short nStatus
                            );
int WINAPI DnaGetNextHist( unsigned long ulKey,
                           double *pdValue,
                           char *szTime,    unsigned short nTime,
                           char *szStatus,  unsigned short nStatus
                         );


/********************************/
/* service and point list calls */
/********************************/

/* note: the following list calls are not available in Visual Basic */
int WINAPI DnaGetServiceList( unsigned short nCount,
                              char *szType,
                              char *szStartSvcName,
                              char *szSvcName[],    unsigned short nSvcName,
                              char *szSvcDesc[],    unsigned short nSvcDesc,
                              char *szSvcType[],    unsigned short nSvcType,
                              char *szStatus[],     unsigned short nStatus
                            );
int WINAPI DnaGetPointList( unsigned short nCount,
                            char *szServiceName,
                            unsigned short nStarting,
                            char *szPoint[],    unsigned short nPoint,
                            double *dValue[],
                            char *szTime[],     unsigned short nTime,
                            char *szStatus[],   unsigned short nStatus,
                            char *szDesc[],     unsigned short nDesc,
                            char *szUnits[],    unsigned short nUnits
                          );

/* note: the commands below are accessed by Visual Basic in place of the preceeding */
int WINAPI DnaGetServiceEntry( char *szType,
                               char *szStartSvcName,
                               unsigned long *pulKey,
                               char *szSvcName, unsigned short nSvcName,
                               char *szSvcDesc, unsigned short nSvcDesc,
                               char *szSvcType, unsigned short nSvcType,
                               char *szStatus,  unsigned short nStatus
                             );
int WINAPI DnaGetNextServiceEntry( unsigned long lKey,
                                   char *szSvcName, unsigned short nSvcName,
                                   char *szSvcDesc, unsigned short nSvcDesc,
                                   char *szSvcType, unsigned short nSvcType,
                                   char *szStatus,  unsigned short nStatus
                                 );

int WINAPI DnaGetPointEntry( char *szServiceName,
                             unsigned short nStarting,
                             unsigned long *pulKey,
                             char *szPoint,     unsigned short nPoint,
                             double *pdValue,
                             char *szTime,      unsigned short nTime,
                             char *szStatus,    unsigned short nStatus,
                             char *szDesc,      unsigned short nDesc,
                             char *szUnits,     unsigned short nUnits
                           );
int WINAPI DnaGetNextPointEntry( unsigned long lKey,
                                 char *szPoint,     unsigned short nPoint,
                                 double *pdValue,
                                 char *szTime,      unsigned short nTime,
                                 char *szStatus,    unsigned short nStatus,
                                 char *szDesc,      unsigned short nDesc,
                                 char *szUnits,     unsigned short nUnits
                               );

int WINAPI DnaSelectPoint(char *szPoint, unsigned short nPoint);


/////////////////////////////////////////////////////////////////////////////

