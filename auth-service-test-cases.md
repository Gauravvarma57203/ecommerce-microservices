# ✅ Auth Service Test Cases

## 🔐 REGISTER (`POST /api/auth/register`)

| Test Case ID | Scenario | Input | Expected Output | Status |
|--------------|----------|-------|------------------|--------|
| R1 | Valid registration | Valid name, email (`@gmail.com`), and strong password | 200 OK, User created | ✅ Passed |
| R2 | Duplicate email | Same email as existing user | 400 Bad Request, "Email already exists" | ✅ Passed |
| R3 | Invalid email format | `Vnot-an-email` | 400, "Please use a valid email from..." | ✅ Passed |
| R4 | Blank email | Empty string | 400, "Email is required" | ✅ Passed |
| R5 | Blank name | Valid email and password, blank name | 400, "Name is required" | ✅ Passed |
| R6 | Weak password | Missing upper/lower/special | 400, "Password must contain..." | ✅ Passed |
| R7 | All fields blank | `""` for all fields | 400, all validation errors | ✅ Passed |

---

## 🔑 LOGIN (`POST /api/auth/login`)

| Test Case ID | Scenario | Input | Expected Output | Status |
|--------------|----------|-------|------------------|--------|
| L1 | Valid login | Correct email (case-insensitive) and password | 200 OK, JWT Token | ✅ Passed |
| L2 | Wrong email | Email not in DB | 401 Unauthorized, "User not found" | ✅ Passed |
| L3 | Wrong password | Correct email, wrong password | 401 Unauthorized, "Invalid credentials" | ✅ Passed |
| L4 | Blank email | Email empty | 400, "Email is required" | ✅ Passed |
| L5 | Blank password | Password empty | 400, "Password is required" | ✅ Passed |
| L6 | All fields blank | Both empty | 400, both validation errors | ✅ Passed |

---

## 👤 GET CURRENT USER (`GET /api/auth/me`)

| Test Case ID | Scenario | Header | Expected Output | Status |
|--------------|----------|--------|------------------|--------|
| M1 | Valid JWT token | Bearer Token | 200 OK, User details | ✅ Passed |
| M2 | No token | None | 403 Forbidden | ✅ Passed |
| M3 | Invalid/malformed token | Tampered token | 403 Forbidden | ✅ Passed |

---

## 📧 GET USER BY EMAIL (`GET /api/auth/user?email=`)

| Test Case ID | Scenario | Email Param + Header | Expected Output | Status |
|--------------|----------|-----------------------|------------------|--------|
| U1 | Valid email and token | `email=valid@gmail.com` + Token | 200 OK, User details | ✅ Passed |
| U2 | Valid email but no token | `email=valid@gmail.com` + No Token | 403 Forbidden | ✅ Passed |
| U3 | Valid email and invalid token | `email=valid@gmail.com` + Tampered token | 403 Forbidden | ✅ Passed |

---

## 🔁 Additional Validations

- ✅ Email is stored in lowercase regardless of input casing
- ✅ JWT Token is validated correctly across endpoints
- ✅ Clear API responses with standardized structure:
  ```json
  {
    "success": true/false,
    "message": "...",
    "status": 200/400/401/403,
    "path": "...",
    "data": { ... },
    "timestamp": "..."
  }
