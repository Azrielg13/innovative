CREATE OR REPLACE VIEW `DataFileView` AS
	SELECT DataFile.*
	FROM DataFile;

CREATE OR REPLACE VIEW `GeneralDataView` AS
	SELECT GeneralData.*
	FROM GeneralData;

CREATE OR REPLACE VIEW `UserView` AS
	SELECT User.*, concat(first_name, ' ', last_name) AS full_name
	FROM User;

CREATE OR REPLACE VIEW `NurseView` AS
	SELECT User.first_name, User.last_name, User.full_name, User.email, User.user_name, User.notes, User.last_login,
	    Nurse.*
	FROM Nurse, UserView AS User
	WHERE Nurse.id = User.id;

CREATE OR REPLACE VIEW `LicenseView` AS
    SELECT License.*, UserView.full_name AS nurse_name, GeneralData.name AS lic_type_name
    FROM License, UserView, GeneralData
    WHERE License.nurse_id = UserView.id AND License.lic_type_id = GeneralData.id;

CREATE OR REPLACE VIEW `PatientView` AS
    SELECT Patient.*, Vendor.NAME AS billing_vendor_name
    FROM Patient, Vendor
    WHERE Patient.billing_id = vendor.id;

CREATE OR REPLACE VIEW `AppointmentView` AS
    SELECT Appointment.*, UserView.full_name AS nurse_name,
        Patient.name AS patient_name, Patient.billing_id AS vendor_id, Patient.billing_vendor_name AS vendor_name
    FROM Appointment, UserView, PatientView AS Patient
    WHERE Appointment.nurse_id = UserView.id AND Appointment.patient_id = Patient.id;

CREATE OR REPLACE VIEW `PaystubView` AS
    SELECT Paystub.*, UserView.full_name AS nurse_name
    FROM Paystub, UserView
    WHERE Paystub.nurse_id = UserView.id;

CREATE OR REPLACE VIEW `InvoiceView` AS
	SELECT Invoice.*
	FROM Invoice;

CREATE OR REPLACE VIEW `VendorView` AS
	SELECT Vendor.*
	FROM Vendor;

CREATE OR REPLACE VIEW `TransHistView` AS
	SELECT TransHist.*
	FROM TransHist;

CREATE OR REPLACE VIEW `ActiveSessionView` AS
	SELECT ActiveSession.*
	FROM ActiveSession;