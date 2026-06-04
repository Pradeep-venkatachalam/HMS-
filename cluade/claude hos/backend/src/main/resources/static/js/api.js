const API_BASE = 'http://localhost:8080/api';

const api = {
    getToken() {
        return localStorage.getItem('token');
    },

    getUser() {
        const u = localStorage.getItem('user');
        return u ? JSON.parse(u) : null;
    },

    isLoggedIn() {
        return !!this.getToken();
    },

    hasRole(...roles) {
        const user = this.getUser();
        return user && roles.includes(user.role);
    },

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '../../../../../../index.html';
    },

    async request(method, path, body = null) {
        const token = this.getToken();
        const headers = { 'Content-Type': 'application/json' };
        if (token) headers['Authorization'] = `Bearer ${token}`;

        const options = { method, headers };
        if (body) options.body = JSON.stringify(body);

        try {
            const res = await fetch(`${API_BASE}${path}`, options);
            const data = await res.json();

            if (res.status === 401) {
                this.logout();
                return null;
            }

            if (!res.ok) {
                throw new Error(data.message || 'Request failed');
            }

            return data;
        } catch (err) {
            if (err.name !== 'TypeError') throw err;
            throw new Error('Cannot connect to server. Make sure the backend is running.');
        }
    },

    get(path)         { return this.request('GET', path); },
    post(path, body)  { return this.request('POST', path, body); },
    put(path, body)   { return this.request('PUT', path, body); },
    patch(path, body) { return this.request('PATCH', path, body); },
    delete(path)      { return this.request('DELETE', path); },

    // Auth
    login(creds)       { return this.post('/auth/login', creds); },
    register(data)     { return this.post('/auth/register', data); },

    // Dashboard
    getDashboard()     { return this.get('/dashboard/stats'); },

    // Patients
    getPatients()                      { return this.get('/patients'); },
    getPatient(id)                     { return this.get(`/patients/${id}`); },
    createPatient(data)                { return this.post('/patients', data); },
    updatePatient(id, data)            { return this.put(`/patients/${id}`, data); },
    deletePatient(id)                  { return this.delete(`/patients/${id}`); },
    searchPatients(name)               { return this.get(`/patients/search?name=${encodeURIComponent(name)}`); },
    getPatientByUserId(uid)            { return this.get(`/patients/user/${uid}`); },

    // Doctors
    getDoctors()                       { return this.get('/doctors'); },
    getDoctor(id)                      { return this.get(`/doctors/${id}`); },
    createDoctor(data)                 { return this.post('/doctors', data); },
    updateDoctor(id, data)             { return this.put(`/doctors/${id}`, data); },
    deleteDoctor(id)                   { return this.delete(`/doctors/${id}`); },
    searchDoctors(name)                { return this.get(`/doctors/search?name=${encodeURIComponent(name)}`); },

    // Appointments
    getAppointments()                  { return this.get('/appointments'); },
    getAppointment(id)                 { return this.get(`/appointments/${id}`); },
    createAppointment(data)            { return this.post('/appointments', data); },
    updateAppointment(id, data)        { return this.put(`/appointments/${id}`, data); },
    updateAppointmentStatus(id, status){ return this.patch(`/appointments/${id}/status?status=${status}`); },
    deleteAppointment(id)              { return this.delete(`/appointments/${id}`); },
    getAppointmentsByPatient(pid)      { return this.get(`/appointments/patient/${pid}`); },
    getAppointmentsByDoctor(did)       { return this.get(`/appointments/doctor/${did}`); },
    getAppointmentsByDate(date)        { return this.get(`/appointments/date?date=${date}`); },

    // Prescriptions
    getPrescriptions()                 { return this.get('/prescriptions'); },
    getPrescription(id)                { return this.get(`/prescriptions/${id}`); },
    getPrescriptionByAppointment(aid)  { return this.get(`/prescriptions/appointment/${aid}`); },
    createPrescription(data)           { return this.post('/prescriptions', data); },
    updatePrescription(id, data)       { return this.put(`/prescriptions/${id}`, data); },
    getPrescriptionsByPatient(pid)     { return this.get(`/prescriptions/patient/${pid}`); },

    // Bills
    getBills()                         { return this.get('/bills'); },
    getBill(id)                        { return this.get(`/bills/${id}`); },
    getBillByAppointment(aid)          { return this.get(`/bills/appointment/${aid}`); },
    createBill(data)                   { return this.post('/bills', data); },
    updateBill(id, data)               { return this.put(`/bills/${id}`, data); },
    markBillPaid(id, method)           { return this.patch(`/bills/${id}/pay?paymentMethod=${method}`); },
    getBillsByPatient(pid)             { return this.get(`/bills/patient/${pid}`); },
};
