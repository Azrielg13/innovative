/* JavaDNA. A EasyDNA C++ to Java Interface
 * written by Eddie Mayfield June 2002
 * (C) 2002 Southern California Edison
 */

#include "com_sce_javadna_JavaDNA.h"
#include "windows.h"
#include "ezdnaapi.h"

/*char* getCharArrayPointer(JNIEnv *_env, jstring _jstr){
	const char *szPoint = _env->GetStringUTFChars(_jstr,0);
	int size = _env->GetStringUTFLength(_jstr);
	char *szP = new char[size+1];
	for(int x=0; x<size; x++)
		szP[x] = *(szPoint+x);
	szP[size] = 0;
	_env->ReleaseStringUTFChars(_jstr,szPoint);
	return szP;
}*/

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    isGoodPointFormat
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_sce_javadna_JavaDNA_isGoodPointFormat(JNIEnv *_env, jclass jc, jstring _szPoint){
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	int ret = DNAGoodPointFormat(szP);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jboolean)(ret == -1);
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTValue
 * Signature: (Ljava/lang/String;)D
 */
JNIEXPORT jdouble JNICALL Java_com_sce_javadna_JavaDNA_getRTValue(JNIEnv *_env, jclass jc, jstring _szPoint){
	double value=0.0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTValue(szP, &value);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jdouble)value;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTTime
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_getRTTime(JNIEnv *_env, jclass jc, jstring _szPoint){
	char result[21];
	result[20] = 0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTTime(szP, result, sizeof(result)-1);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return _env->NewStringUTF(result);
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTTimeUTC
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_getRTTimeUTC(JNIEnv *_env, jclass jc, jstring _szPoint){
	long value=0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTTimeUTC(szP, &value);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jlong)value;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTStatus
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_getRTStatus(JNIEnv *_env, jclass jc, jstring _szPoint){
	char result[21];
	result[20] = 0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTStatus(szP, result, sizeof(result)-1);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return _env->NewStringUTF(result);
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTDesc
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_getRTDesc(JNIEnv *_env, jclass jc, jstring _szPoint){
	char result[31];
	result[30] = 0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTDesc(szP, result, sizeof(result)-1);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return _env->NewStringUTF(result);
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getRTUnits
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_getRTUnits(JNIEnv *_env, jclass jc, jstring _szPoint){
	char result[11];
	result[10] = 0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DNAGetRTUnits(szP, result, sizeof(result)-1);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return _env->NewStringUTF(result);
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistAvg
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistAvg(JNIEnv *_env, jclass jc, jstring _szPoint, jstring _sTime, jstring _eTime, jstring _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	char *sTime = (char*)_env->GetStringUTFChars(_sTime,0);
	char *eTime = (char*)_env->GetStringUTFChars(_eTime,0);
	char *period = (char*)_env->GetStringUTFChars(_period,0);
	DnaGetHistAvg(szP,sTime,eTime,period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	_env->ReleaseStringUTFChars(_sTime,sTime);
	_env->ReleaseStringUTFChars(_eTime,eTime);
	_env->ReleaseStringUTFChars(_period,period);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistAvgUTC
 * Signature: (Ljava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistAvgUTC(JNIEnv *_env, jclass jc, jstring _szPoint, jlong _sTime, jlong _eTime, jlong _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DnaGetHistAvgUTC(szP,(long)_sTime,(long)_eTime,(long)_period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistSnap
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistSnap(JNIEnv *_env, jclass jc, jstring _szPoint, jstring _sTime, jstring _eTime, jstring _period){
	unsigned long pulKey=0;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	char *sTime = (char*)_env->GetStringUTFChars(_sTime,0);
	char *eTime = (char*)_env->GetStringUTFChars(_eTime,0);
	char *period = (char*)_env->GetStringUTFChars(_period,0);
	DnaGetHistSnap(szP,sTime,eTime,period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	_env->ReleaseStringUTFChars(_sTime,sTime);
	_env->ReleaseStringUTFChars(_eTime,eTime);
	_env->ReleaseStringUTFChars(_period,period);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistSnapUTC
 * Signature: (Ljava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistSnapUTC(JNIEnv *_env, jclass jc, jstring _szPoint, jlong _sTime, jlong _eTime, jlong _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DnaGetHistSnapUTC(szP,(long)_sTime,(long)_eTime,(long)_period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistMax
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistMax(JNIEnv *_env, jclass jc, jstring _szPoint, jstring _sTime, jstring _eTime, jstring _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	char *sTime = (char*)_env->GetStringUTFChars(_sTime,0);
	char *eTime = (char*)_env->GetStringUTFChars(_eTime,0);
	char *period = (char*)_env->GetStringUTFChars(_period,0);
	DnaGetHistMax(szP,sTime,eTime,period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	_env->ReleaseStringUTFChars(_sTime,sTime);
	_env->ReleaseStringUTFChars(_eTime,eTime);
	_env->ReleaseStringUTFChars(_period,period);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistMaxUTC
 * Signature: (Ljava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistMaxUTC(JNIEnv *_env, jclass jc, jstring _szPoint, jlong _sTime, jlong _eTime, jlong _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DnaGetHistMaxUTC(szP,(long)_sTime,(long)_eTime,(long)_period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistMin
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistMin(JNIEnv *_env, jclass jc, jstring _szPoint, jstring _sTime, jstring _eTime, jstring _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	char *sTime = (char*)_env->GetStringUTFChars(_sTime,0);
	char *eTime = (char*)_env->GetStringUTFChars(_eTime,0);
	char *period = (char*)_env->GetStringUTFChars(_period,0);
	DnaGetHistMin(szP,sTime,eTime,period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	_env->ReleaseStringUTFChars(_sTime,sTime);
	_env->ReleaseStringUTFChars(_eTime,eTime);
	_env->ReleaseStringUTFChars(_period,period);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    initHistMinUTC
 * Signature: (Ljava/lang/String;JJJ)J
 */
JNIEXPORT jlong JNICALL Java_com_sce_javadna_JavaDNA_initHistMinUTC(JNIEnv *_env, jclass jc, jstring _szPoint, jlong _sTime, jlong _eTime, jlong _period){
	unsigned long pulKey;
	char *szP = (char*)_env->GetStringUTFChars(_szPoint,0);
	DnaGetHistMinUTC(szP,(long)_sTime,(long)_eTime,(long)_period,&pulKey);
	_env->ReleaseStringUTFChars(_szPoint,szP);
	return (jlong)pulKey;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getNextHist
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_sce_javadna_JavaDNA_getNextHist(JNIEnv *_env, jclass jc, jlong _ulKey){
	double value;
	char szTime[20], szStatus[20];
	DnaGetNextHist((unsigned long)_ulKey,&value,szTime,sizeof(szTime),szStatus,sizeof(szStatus));
	unsigned int x=0, offset=0, size=(sizeof value)+sizeof(szTime)+sizeof(szStatus);
	if(value < 0){
		value = 0;
		strcpy(szStatus,"BAD POINT / NO DATA\0");
	}
	if(szTime[0] < '0' || szTime[0] > '9'){//|| (szStatus[2] < 'A' && szStatus[2] != 0) || szStatus[2] > 'Z'){
		strcpy(szTime,"00/00/00 00:00:00\0");
		strcpy(szStatus,"BAD POINT / NO DATA\0");
	}
	jbyte *buf = new jbyte[size];
	char *p = (char*)&value;
	for(;x<(sizeof value); x++)
		buf[x] = *(p+x);
	offset = x;
	for(;x<(offset+sizeof(szTime)); x++)
		buf[x] = szTime[x-offset];
	offset = x;
	for(;x<size; x++)
		buf[x] = (jbyte)szStatus[x-offset];
	jbyteArray bA = _env->NewByteArray(size);
	_env->SetByteArrayRegion(bA,0,size,buf);
	delete [] buf;
	return bA;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getNextHistUTC
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_sce_javadna_JavaDNA_getNextHistUTC(JNIEnv *_env, jclass jc, jlong _ulKey){
	double value;
	long szTime;
	char szStatus[20];
	DnaGetNextHistUTC((unsigned long)_ulKey,&value,&szTime,szStatus,sizeof(szStatus));
	jlong time = (jlong)szTime;
	int size = (sizeof value)+(sizeof time)+sizeof(szStatus);
	jbyte *buf = new jbyte[size];
	int x=0,offset=0;
	char *p = (char*)&value;
	for(;x<(sizeof value); x++)
		buf[x] = *(p+x);
	offset = x;
	for(;x<(sizeof time)+(sizeof value); x++)
		buf[x] = (jbyte)time>>(x-offset)*8;
	offset = x;
	for(; x<size; x++)
		buf[x] = (jbyte)szStatus[x-offset];
	jbyteArray bA = _env->NewByteArray(size);
	_env->SetByteArrayRegion(bA,0,size,buf);
	delete [] buf;
	return bA;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getServiceEntry
 * Signature: (Ljava/lang/String;Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_sce_javadna_JavaDNA_getServiceEntry(JNIEnv *_env, jclass jc, jstring _szType, jstring _szStartSvcName){
	char* szType = (char*)_env->GetStringUTFChars(_szType,0);
	char* szStartSvcName = (char*)_env->GetStringUTFChars(_szStartSvcName,0);
	unsigned long pulKey;
	char szSvcName[30], szSvcDesc[30], szSvcType[30],szStatus[30];
	DnaGetServiceEntry(szType,szStartSvcName,&pulKey,szSvcName,sizeof(szSvcName),szSvcDesc,sizeof(szSvcDesc),szSvcType,sizeof(szSvcType),szStatus,sizeof(szStatus));
	jlong jpulKey = (jlong)pulKey;
	int size = (sizeof jpulKey)+sizeof(szSvcName)+sizeof(szSvcDesc)+sizeof(szSvcType)+sizeof(szStatus);
	jbyte *buf = new jbyte[size];
	int x=0, offset=0;
	for(;x<(sizeof jpulKey); x++)
		buf[x] = (jbyte)(jpulKey>>((x-offset)*8));
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szSvcName[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szSvcDesc[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szSvcType[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szStatus[x-offset];
	jbyteArray bA = _env->NewByteArray(size);
	_env->SetByteArrayRegion(bA,0,size,buf);
	_env->ReleaseStringUTFChars(_szType,szType);
	_env->ReleaseStringUTFChars(_szStartSvcName,szStartSvcName);
	delete [] buf;
	return bA;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getNextServiceEntry
 * Signature: (J)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_com_sce_javadna_JavaDNA_getNextServiceEntry(JNIEnv *_env, jclass jc, jlong _lKey){
	char szSvcName[31], szSvcDesc[31], szSvcType[31], szStatus[31];
	szSvcName[30] = 0;
	szSvcDesc[30] = 0;
	szSvcType[30] = 0;
	szStatus[30] = 0;
	DnaGetNextServiceEntry((unsigned long)_lKey,szSvcName,30,szSvcDesc,30,szSvcType,30,szStatus,30);
	jobjectArray ret = (jobjectArray)_env->NewObjectArray(4,_env->FindClass("java/lang/String"),_env->NewStringUTF(""));
	_env->SetObjectArrayElement(ret,0,_env->NewStringUTF(szSvcName));
	_env->SetObjectArrayElement(ret,1,_env->NewStringUTF(szSvcDesc));
	_env->SetObjectArrayElement(ret,2,_env->NewStringUTF(szSvcType));
  	_env->SetObjectArrayElement(ret,3,_env->NewStringUTF(szStatus));
	return ret;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getPointEntry
 * Signature: (Ljava/lang/String;S)[Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_sce_javadna_JavaDNA_getPointEntry(JNIEnv *_env, jclass jc, jstring _szName , jshort _nStarting){
	char* szName = (char*)_env->GetStringUTFChars(_szName,0);
	unsigned long pulKey;
	double value;
	char szPoint[30], szTime[30], szStatus[30], szDesc[30], szUnits[30];
	DnaGetPointEntry(szName, (unsigned short)_nStarting, &pulKey, szPoint, sizeof(szPoint), &value, szTime, sizeof(szTime), szStatus, sizeof(szStatus), szDesc, sizeof(szDesc), szUnits, sizeof(szUnits));
	jlong jpulKey = (jlong)pulKey;
	int size = (sizeof jpulKey)+sizeof(szPoint)+(sizeof value)+sizeof(szTime)+sizeof(szStatus)+sizeof(szDesc)+sizeof(szUnits);
	jbyte *buf = new jbyte[size];
	unsigned int x=0, offset=0;
	for(;x<(sizeof jpulKey); x++)
		buf[x] = (jbyte)(jpulKey>>((x-offset)*8));
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szPoint[x-offset];
	offset = x;
	char *p = (char*)&value;
	for(;x<(offset+(sizeof value)); x++)
		buf[x] = *(p+x-offset);
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szTime[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szStatus[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szDesc[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szUnits[x-offset];
	jbyteArray bA = _env->NewByteArray(size);
	_env->SetByteArrayRegion(bA,0,size,buf);
	delete [] buf;
	_env->ReleaseStringUTFChars(_szName,szName);
	return bA;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    getNextPointEntry
 * Signature: (J)[Ljava/lang/String;
 */
JNIEXPORT jbyteArray JNICALL Java_com_sce_javadna_JavaDNA_getNextPointEntry(JNIEnv *_env, jclass jc, jlong _ulKey){
	char szPoint[30], szTime[30], szStatus[30], szDesc[30], szUnits[30];
	double value;
	DnaGetNextPointEntry((unsigned long)_ulKey, szPoint, sizeof(szPoint), &value, szTime, sizeof(szTime), szStatus, sizeof(szStatus), szDesc, sizeof(szDesc), szUnits, sizeof(szUnits));
	int size = sizeof(szPoint)+(sizeof value)+sizeof(szTime)+sizeof(szStatus)+sizeof(szDesc)+sizeof(szUnits);
	jbyte *buf = new jbyte[size];
	unsigned int x=0, offset=0;
	for(;x<30; x++)
		buf[x] = (jbyte)szPoint[x-offset];
	offset = x;
	char *p = (char*)&value;
	for(;x<(offset+(sizeof value)); x++)
		buf[x] = *(p+x-offset);
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szTime[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szStatus[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szDesc[x-offset];
	offset = x;
	for(;x<offset+30; x++)
		buf[x] = (jbyte)szUnits[x-offset];
	jbyteArray bA = _env->NewByteArray(size);
	_env->SetByteArrayRegion(bA,0,size,buf);
	delete [] buf;
	return bA;
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    selectPoint
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_selectPoint__(JNIEnv *_env, jclass jc){
	char pszPoint[31];
	unsigned short nPoint=30;
	pszPoint[nPoint] = 0;
	int ret = DnaSelectPoint(pszPoint, nPoint);
	if(ret==0)
		return _env->NewStringUTF(pszPoint);
	else
		return _env->NewStringUTF("");
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    selectPoint
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_sce_javadna_JavaDNA_selectPoint__Ljava_lang_String_2(JNIEnv *_env, jclass jc, jstring _point){
	//char* point = getCharArrayPointer(_env,_point);
	char pszPoint[31];
	unsigned short nPoint=30;
	//strcpy(pszPoint,point);
	pszPoint[nPoint] = 0;
	int ret = DnaSelectPoint(pszPoint, nPoint);
	if(ret==0)
		return _env->NewStringUTF(pszPoint);
	else
		return _env->NewStringUTF("");
}

/*
 * Class:     com_sce_javadna_JavaDNA
 * Method:    parseDouble
 * Signature: ([B)D
 */
JNIEXPORT jdouble JNICALL Java_com_sce_javadna_JavaDNA_parseDouble(JNIEnv *_env, jclass jc, jbyteArray _value){
	jdouble value=0;
	jbyte *buf = _env->GetByteArrayElements(_value,NULL);
	char *p = (char*)(&value);
	for(int x=0; x<sizeof value; x++)
		*(p+x) = buf[x];
	_env->ReleaseByteArrayElements(_value,buf,JNI_ABORT);
	//_env->ReleaseStringUTFChars(_jstr,szPoint);
	return value;
}