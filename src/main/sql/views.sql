CREATE OR REPLACE VIEW V_USER AS
	SELECT user.*, concat(FIRST_NAME, ' ', LAST_NAME) AS FULL_NAME
	FROM user;

CREATE OR REPLACE VIEW V_NURSE AS
	SELECT User.FIRST_NAME, User.LAST_NAME, User.FULL_NAME, User.EMAIL, User.USER_NAME, User.NOTES, User.LAST_LOGIN,
	    nurse.*
	FROM nurse, V_USER AS User
	WHERE nurse.ID = User.ID;

CREATE OR REPLACE VIEW V_LICENSE AS
    SELECT license.*, V_USER.FULL_NAME AS NURSE_NAME, GeneralData.name AS Lic_Type_Name
    FROM license, V_USER, GeneralData
    WHERE license.NURSE_ID = V_USER.ID AND license.lic_type_id = GeneralData.id;

CREATE OR REPLACE VIEW V_PATIENT AS
    SELECT patient.*, vendor.NAME AS BILLING_VENDOR_NAME
    FROM patient, vendor
    WHERE patient.BILLING_ID = vendor.ID;

CREATE OR REPLACE VIEW V_APPOINTMENT AS
    SELECT appointment.*, User.FULL_NAME AS NURSE_NAME,
        Patient.NAME AS PATIENT_NAME, Patient.BILLING_ID AS VENDOR_ID, Patient.BILLING_VENDOR_NAME AS VENDOR_NAME
    FROM appointment, V_USER AS User, V_PATIENT AS Patient
    WHERE appointment.NURSE_ID = User.ID
        AND appointment.PATIENT_ID = Patient.ID;

CREATE OR REPLACE VIEW V_PAYSTUB AS
    SELECT paystub.*, V_USER.FULL_NAME AS NURSE_NAME
    FROM paystub, V_USER
    WHERE paystub.NURSE_ID = V_USER.ID;
