/* For local development use http://localhost:8080. On Netlify, /api is proxied by netlify.toml. */
// A deployed or Java-served portal uses its same-origin /api path. A file opened
// directly still uses the local API fallback for simple development previews.
let API = window.BCREC_API_BASE_URL || (location.protocol === "file:" ? "http://localhost:8080" : "");
let students = [];
const $ = (id) => document.getElementById(id);

async function request(path, options = {}) {
  const response = await fetch(`${API}${path}`, options);
  // Some local machines already reserve port 8080 (for example, Tomcat).
  // Fall back to the portal's alternate local port without affecting production.
  if (!response.ok && API === "http://localhost:8080" && response.status === 404) {
    API = "http://localhost:18080";
    return request(path, options);
  }
  if (!response.ok) {
    let message = "Request failed. Please try again.";
    try { message = (await response.json()).message || message; } catch (_) { /* non-JSON error */ }
    throw new Error(message);
  }
  return response.status === 204 ? null : response.json();
}

function displayText(value) { return value == null || value === "" ? "—" : String(value); }
function renderRows(items) {
  const body = document.querySelector("#studentsTable tbody");
  body.replaceChildren();
  $("emptyState").classList.toggle("is-hidden", items.length > 0);
  document.querySelector(".table-wrap").classList.toggle("is-hidden", items.length === 0);
  items.forEach((student) => {
    const row = document.createElement("tr");
    const values = [
      [`${displayText(student.firstName)} ${displayText(student.lastName)}`, student.email],
      [student.rollNumber], [student.phone], [student.department], [`Semester ${student.year}`], [Number(student.cgpa || 0).toFixed(2)]
    ];
    values.forEach(([main, sub], index) => {
      const cell = document.createElement("td");
      const primary = document.createElement(index === 3 ? "span" : "div");
      primary.textContent = main;
      if (index === 0) primary.className = "student-name";
      if (index === 3) primary.className = "pill";
      cell.append(primary);
      if (sub) { const detail = document.createElement("small"); detail.textContent = sub; detail.className = index === 0 ? "" : "contact"; cell.append(detail); }
      row.append(cell);
    });
    const actions = document.createElement("td"); actions.className = "actions";
    const edit = document.createElement("button"); edit.type = "button"; edit.className = "action"; edit.textContent = "Edit"; edit.addEventListener("click", () => openForm(student));
    const remove = document.createElement("button"); remove.type = "button"; remove.className = "action delete"; remove.textContent = "Delete"; remove.addEventListener("click", () => deleteStudent(student));
    actions.append(edit, remove); row.append(actions); body.append(row);
  });
}

async function loadStudents() {
  $("loadingState").classList.remove("is-hidden"); $("errorState").classList.add("is-hidden");
  try { students = await request("/api/students"); renderRows(filterStudents()); await loadStats(); }
  catch (error) { $("errorText").textContent = error.message; $("errorState").classList.remove("is-hidden"); }
  finally { $("loadingState").classList.add("is-hidden"); }
}
async function loadStats() {
  try { const stats = await request("/api/stats"); $("totalStudents").textContent = stats.totalStudents; $("totalCourses").textContent = stats.totalCourses; $("averageCgpa").textContent = Number(stats.averageCgpa || 0).toFixed(2); } catch (_) { /* table remains usable */ }
}
function filterStudents() {
  const term = $("searchInput").value.trim().toLowerCase();
  if (!term) return students;
  return students.filter((s) => [s.rollNumber, s.firstName, s.lastName, s.email, s.department].some((value) => String(value || "").toLowerCase().includes(term)));
}
function openForm(student = null) {
  $("studentForm").reset(); $("formError").classList.add("is-hidden");
  $("studentId").value = student?.id || ""; $("formTitle").textContent = student ? "Edit student" : "Add student"; $("formEyebrow").textContent = student ? "UPDATE RECORD" : "NEW RECORD"; $("submitButton").textContent = student ? "Save changes" : "Save student";
  ["rollNumber", "firstName", "lastName", "email", "phone", "department", "year", "cgpa", "address"].forEach((field) => { $(field).value = student?.[field] ?? (field === "cgpa" ? 0 : ""); });
  $("rollNumber").readOnly = Boolean(student); $("modal").classList.remove("is-hidden"); $("firstName").focus();
}
function closeForm() { $("modal").classList.add("is-hidden"); }
function showError(message) { $("formError").textContent = message; $("formError").classList.remove("is-hidden"); }
function toast(message, error = false) { const item = $("toast"); item.textContent = message; item.className = `toast${error ? " error" : ""}`; setTimeout(() => item.classList.add("is-hidden"), 3500); }
async function saveStudent(event) {
  event.preventDefault(); const form = $("studentForm");
  if (!form.checkValidity()) { form.reportValidity(); return; }
  const id = $("studentId").value;
  const payload = Object.fromEntries(["rollNumber", "firstName", "lastName", "email", "phone", "department", "address"].map((key) => [key, $(key).value.trim()]));
  payload.year = Number($("year").value); payload.cgpa = Number($("cgpa").value || 0);
  try { await request(id ? `/api/students/${id}` : "/api/students", { method: id ? "PUT" : "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(payload) }); closeForm(); toast(id ? "Student record updated." : "Student added successfully."); await loadStudents(); }
  catch (error) { showError(error.message); }
}
async function deleteStudent(student) {
  if (!confirm(`Are you sure you want to delete ${student.firstName} ${student.lastName}?`)) return;
  try { await request(`/api/students/${student.id}`, { method: "DELETE" }); toast("Student record deleted."); await loadStudents(); }
  catch (error) { toast(error.message, true); }
}
document.querySelectorAll("[data-open-form]").forEach((button) => button.addEventListener("click", () => openForm()));
$("closeModal").addEventListener("click", closeForm); $("cancelForm").addEventListener("click", closeForm); $("studentForm").addEventListener("submit", saveStudent); $("searchInput").addEventListener("input", () => renderRows(filterStudents())); $("retryButton").addEventListener("click", loadStudents);
$("modal").addEventListener("click", (event) => { if (event.target === $("modal")) closeForm(); });
loadStudents();
