CREATE OR REPLACE VIEW V_NURSE AS
	SELECT USER.FIRST_NAME, USER.LAST_NAME, USER.EMAIL, USER.NOTES, USER.LAST_LOGIN, NURSE.*
	FROM NURSE, USER
	WHERE NURSE.ID = USER.ID;

CREATE OR REPLACE VIEW V_APPOINTMENT AS
    SELECT Appointment.*, concat(User.FIRST_NAME, ' ', User.LAST_NAME) AS NURSE_NAME,
        Patient.NAME AS PATIENT_NAME, Vendor.ID AS VENDOR_ID, Vendor.NAME AS VENDOR_NAME
    FROM Appointment LEFT JOIN User ON Appointment.NURSE_ID = User.ID
        LEFT JOIN Patient ON Appointment.PATIENT_ID = Patient.ID
        LEFT JOIN Vendor ON Patient.BILLING_ID = Vendor.ID;

CREATE OR REPLACE VIEW V_PATIENT AS
    SELECT Patient.*, Vendor.NAME AS BILLING_VENDOR_NAME
    FROM Patient
    LEFT JOIN Vendor ON Patient.BILLING_ID = Vendor.ID;