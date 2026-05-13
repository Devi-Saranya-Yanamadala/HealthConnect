import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const ROLES = [
  { value: "ADMIN",              label: "Administrator"     },
  { value: "DOCTOR",             label: "Doctor"            },
  { value: "RECEPTION",          label: "Receptionist"      },
  { value: "NURSE",              label: "Nurse"             },
  { value: "BILLING",            label: "Billing Officer"   },
  { value: "COMPLIANCE_OFFICER", label: "Compliance Officer"},
];
const ROLE_DASHBOARDS = {
  admin: "/dashboard/admin", doctor: "/dashboard/doctor",
  reception: "/dashboard/reception", nurse: "/dashboard/nurse",
  billing: "/dashboard/billing", compliance_officer: "/dashboard/compliance",
};

/* ─────────────────────────────────────────────────────────────
   CSS — injected as FIRST child of <head> so it loads before
   Bootstrap. Style ID changed to "hc-v8" to bust any cache.
───────────────────────────────────────────────────────────── */
const CSS = `
@import url('https://fonts.googleapis.com/css2?family=DM+Sans:wght@300;400;500;600;700&family=Space+Grotesk:wght@500;600;700&display=swap');

html, body { margin: 0 !important; padding: 0 !important; overflow: hidden !important; }

/* Full-viewport fixed overlay — escapes every Bootstrap container */
.hc-root {
  position: fixed !important;
  inset: 0 !important;
  display: flex !important;
  flex-direction: row !important;
  font-family: 'DM Sans', sans-serif;
  background: linear-gradient(135deg, #0A1628 0%, #0D2145 50%, #0A2E3D 100%);
  z-index: 9999;
}

/* ── LEFT PANEL ── */
.hc-left {
  flex: 1 1 0% !important;
  min-width: 0;
  height: 100% !important;
  display: flex !important;
  flex-direction: column !important;
  justify-content: center !important;
  align-items: flex-start !important;
  padding: 50px 60px !important;
  position: relative !important;
  overflow: hidden !important;
}
.hc-left::before {
  content: ''; position: absolute; inset: 0; pointer-events: none;
  background:
    radial-gradient(ellipse at 20% 50%, rgba(0,201,167,.08) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 20%, rgba(56,100,255,.08) 0%, transparent 60%);
}
.hc-grid {
  position: absolute; inset: 0; opacity: .04; pointer-events: none;
  background-image: linear-gradient(#00C9A7 1px, transparent 1px),
                    linear-gradient(90deg, #00C9A7 1px, transparent 1px);
  background-size: 40px 40px;
}
.hc-ecg { position: absolute; bottom: 60px; left: 0; right: 0; opacity: .15; pointer-events: none; }
.hc-stat {
  background: rgba(255,255,255,.05); border: 1px solid rgba(255,255,255,.08);
  border-radius: 12px; padding: 13px 20px; width: 100%; max-width: 380px;
}

/* ── RIGHT PANEL ──
   Always scrollable from top. Login centres via margin:auto trick.  */
.hc-right {
  flex: 0 0 460px !important;
  width: 460px !important;
  height: 100% !important;
  background: #fff !important;
  display: flex !important;
  flex-direction: column !important;
  justify-content: flex-start !important;
  padding: 0 44px !important;
  box-shadow: -20px 0 60px rgba(0,0,0,.25) !important;
  overflow-y: auto !important;
  box-sizing: border-box !important;
}
/* Inner content wrapper — centres login, top-aligns register */
.hc-right-inner {
  width: 100%;
  padding: 32px 0;
  box-sizing: border-box;
}
.hc-right-inner.login {
  margin: auto;   /* pushes to vertical centre when content is short */
}

/* ── TABS ── */
.hc-tabs { display: flex; background: #F0F5FF; border-radius: 12px; padding: 4px; margin-bottom: 16px; }
.hc-tab {
  flex: 1; padding: 9px 0; border: none; background: transparent;
  border-radius: 9px; font-size: 14px; font-weight: 600;
  cursor: pointer; font-family: 'DM Sans', sans-serif; color: #7E8EA6; transition: all .2s;
}
.hc-tab.on { background: #fff; color: #00C9A7; box-shadow: 0 2px 8px rgba(0,0,0,.1); }

/* ── FIELD LABEL ── */
.hc-lbl {
  display: block !important; font-size: 11px !important; font-weight: 600 !important;
  letter-spacing: .08em !important; text-transform: uppercase !important;
  color: #7E8EA6 !important; margin-bottom: 5px !important; text-align: left !important;
}

/* ── FIELD WRAPPER ── */
.hc-wrap { position: relative; display: flex; align-items: center; margin-bottom: 10px; }
.hc-il { position: absolute; left: 13px; color: #7E8EA6; display: flex; align-items: center; pointer-events: none; z-index: 3; }
.hc-ir { position: absolute; right: 12px; color: #7E8EA6; display: flex; align-items: center; cursor: pointer; z-index: 3; }

/* ── INPUTS — beat Bootstrap with compound selector + !important ── */
.hc-root .hc-i,
.hc-root input.hc-i,
.hc-root select.hc-i {
  -webkit-appearance: none !important; -moz-appearance: none !important; appearance: none !important;
  display: block !important; width: 100% !important;
  padding: 10px 14px 10px 40px !important;
  font-size: 14px !important; font-family: 'DM Sans', sans-serif !important;
  line-height: 1.5 !important; color: #1A2340 !important;
  background-color: #F0F5FF !important; background-clip: padding-box !important;
  border: 1.5px solid #E2E8F4 !important; border-radius: 10px !important;
  box-shadow: none !important; -webkit-box-shadow: none !important;
  outline: none !important; transition: border-color .15s, box-shadow .15s !important;
}
.hc-root .hc-i.r { padding-right: 40px !important; }
.hc-root .hc-i:focus {
  background-color: #fff !important; border-color: #00C9A7 !important;
  box-shadow: 0 0 0 3px rgba(0,201,167,.15) !important;
  -webkit-box-shadow: 0 0 0 3px rgba(0,201,167,.15) !important;
  outline: none !important;
}
.hc-root .hc-i::placeholder { color: #A0AEC0 !important; opacity: 1 !important; }
.hc-root .hc-i:-webkit-autofill,
.hc-root .hc-i:-webkit-autofill:hover,
.hc-root .hc-i:-webkit-autofill:focus,
.hc-root .hc-i:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 1000px #F0F5FF inset !important;
  box-shadow: 0 0 0 1000px #F0F5FF inset !important;
  -webkit-text-fill-color: #1A2340 !important;
  border-color: #E2E8F4 !important;
  transition: background-color 5000s ease-in-out 0s !important;
}

/* ── SUBMIT BUTTON ── */
.hc-btn {
  display: block !important; width: 100% !important; padding: 11px !important;
  background: linear-gradient(135deg, #00C9A7 0%, #00A88A 100%) !important;
  border: none !important; border-radius: 11px !important;
  color: #fff !important; font-size: 15px !important; font-weight: 600 !important;
  cursor: pointer !important; letter-spacing: .02em !important;
  font-family: 'DM Sans', sans-serif !important;
  transition: transform .2s, box-shadow .2s !important;
  margin-top: 4px !important; text-align: center !important;
}
.hc-btn:hover:not(:disabled) { transform: translateY(-1px) !important; box-shadow: 0 8px 24px rgba(0,201,167,.35) !important; }
.hc-btn:disabled { opacity: .7 !important; cursor: not-allowed !important; }

/* ── ALERTS ── */
.hc-ok  { display:flex;align-items:center;gap:8px;background:#E6FAF6;color:#00875A;border:1px solid #B3EFE3;border-radius:9px;padding:7px 12px;font-size:13px;font-weight:500;margin-bottom:10px; }
.hc-err { display:flex;align-items:center;gap:8px;background:#FEF2F2;color:#B91C1C;border:1px solid #FECACA;border-radius:9px;padding:7px 12px;font-size:13px;font-weight:500;margin-bottom:10px; }

/* ── SPINNER ── */
.hc-spin { width:16px;height:16px;border:2px solid rgba(255,255,255,.4);border-top-color:#fff;border-radius:50%;animation:hcsp .6s linear infinite;display:inline-block;vertical-align:middle; }
@keyframes hcsp { to { transform: rotate(360deg); } }

/* ── LINK BUTTON ── */
.hc-lnk { background:none !important;border:none !important;padding:0 !important;color:#00C9A7 !important;font-size:13px !important;font-weight:600 !important;cursor:pointer !important;font-family:'DM Sans',sans-serif !important; }

@media (max-width: 768px) {
  .hc-left { display: none !important; }
  .hc-right { flex: 1 1 auto !important; width: 100% !important; }
}
`;

function injectCSS() {
  /* Remove any previous version first, then inject fresh */
  ["hc-auth-css","hc-v8","hc-auth-styles"].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.remove();
  });
  const s = document.createElement("style");
  s.id = "hc-v8";
  s.textContent = CSS;
  document.head.insertBefore(s, document.head.firstChild);
}

/* ── Icons ───────────────────────────────────────────────── */
const PulseIcon  = () => <svg width="28" height="28" viewBox="0 0 28 28" fill="none"><path d="M2 14h4l3-9 4 18 3-12 2 6 2-3h6" stroke="#00C9A7" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/></svg>;
const CrossIcon  = () => <svg width="18" height="18" viewBox="0 0 18 18" fill="none"><rect x="7" y="1" width="4" height="16" rx="2" fill="#00C9A7"/><rect x="1" y="7" width="16" height="4" rx="2" fill="#00C9A7"/></svg>;
const EyeIcon    = ({ open }) => <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round">{open?<><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></>:<><path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/></>}</svg>;
const UserIcon   = () => <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>;
const MailIcon   = () => <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>;
const PhoneIcon  = () => <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"><path d="M22 16.92v3a2 2 0 01-2.18 2 19.79 19.79 0 01-8.63-3.07A19.5 19.5 0 013.07 11 19.79 19.79 0 01.02 2.18 2 2 0 012 0h3a2 2 0 012 1.72c.127.96.361 1.903.7 2.81a2 2 0 01-.45 2.11L6.91 7.91a16 16 0 006.72 6.72l1.28-1.27a2 2 0 012.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0122 16.92z"/></svg>;
const LockIcon   = () => <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"/><path d="M7 11V7a5 5 0 0110 0v4"/></svg>;
const ShieldIcon = () => <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>;

/* ── Field components ────────────────────────────────────── */
const Field = ({ icon: Icon, label, type = "text", value, onChange, placeholder, right }) => (
  <div>
    <label className="hc-lbl">{label}</label>
    <div className="hc-wrap">
      <span className="hc-il"><Icon /></span>
      <input type={type} value={value} onChange={onChange} placeholder={placeholder}
        className={`hc-i${right ? " r" : ""}`} autoComplete="off" />
      {right && <span className="hc-ir">{right}</span>}
    </div>
  </div>
);

const Sel = ({ icon: Icon, label, value, onChange, options }) => (
  <div>
    <label className="hc-lbl">{label}</label>
    <div className="hc-wrap">
      <span className="hc-il"><Icon /></span>
      <select value={value} onChange={onChange} className="hc-i r"
        style={{ color: value ? "#1A2340" : "#7E8EA6", cursor: "pointer" }}>
        <option value="">Select your role</option>
        {options.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
      </select>
      <span className="hc-ir" style={{ pointerEvents: "none" }}>
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="6 9 12 15 18 9"/></svg>
      </span>
    </div>
  </div>
);

/* ── Main component ──────────────────────────────────────── */
export default function AuthPage() {
  const navigate = useNavigate();
  const [mode,    setMode]    = useState("login");
  const [showPw,  setShowPw]  = useState(false);
  const [lf,      setLf]      = useState({ username: "", password: "" });
  const [rf,      setRf]      = useState({ username: "", password: "", role: "", email: "", phoneNumber: "" });
  const [loading, setLoading] = useState(false);
  const [ok,  setOk]  = useState("");
  const [err, setErr] = useState("");

  useEffect(() => { injectCSS(); }, []);

  const sw = (m) => { setMode(m); setErr(""); setOk(""); setShowPw(false); };

  const login = async (e) => {
    e.preventDefault(); setLoading(true); setErr(""); setOk("");
    try {
      const res  = await fetch("/api/auth/login", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(lf) });
      const data = await res.json();
      if (res.ok) {
        const rk = data.role.toLowerCase();
        setOk("Login successful! Redirecting...");
        localStorage.setItem("accessToken",  data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);
        localStorage.setItem("role", rk);
        setTimeout(() => navigate(ROLE_DASHBOARDS[rk] || "/dashboard", { replace: true }), 1200);
      } else setErr(data.message || "Invalid credentials. Please try again.");
    } catch { setErr("Unable to connect. Please check your network."); }
    finally { setLoading(false); }
  };

  const register = async (e) => {
    e.preventDefault(); setLoading(true); setErr(""); setOk("");
    try {
      const res  = await fetch("/api/auth/register", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(rf) });
      const data = await res.json();
      if (res.ok) { setOk("Account created! You can now log in."); setTimeout(() => { setMode("login"); setOk(""); }, 2000); }
      else setErr(data.message || "Registration failed. Please try again.");
    } catch { setErr("Unable to connect. Please check your network."); }
    finally { setLoading(false); }
  };

  /* Shared inline values */
  const spaceGrotesk = "'Space Grotesk', sans-serif";

  return (
    <div className="hc-root">

      {/* ══ LEFT PANEL ══ */}
      <div className="hc-left">
        <div className="hc-grid" />

        {/* Logo */}
        <div style={{ position: "relative", zIndex: 1, marginBottom: 28 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            <div style={{ width: 48, height: 48, borderRadius: 12, flexShrink: 0, background: "rgba(0,201,167,0.15)", border: "1px solid rgba(0,201,167,0.3)", display: "flex", alignItems: "center", justifyContent: "center" }}>
              <PulseIcon />
            </div>
            <div>
              <div style={{ fontFamily: spaceGrotesk, fontSize: 22, fontWeight: 700, color: "#fff", letterSpacing: "-0.02em" }}>HEALTH CONNECT</div>
              <div style={{ fontSize: 12, color: "#00C9A7", letterSpacing: "0.06em", fontWeight: 500 }}>HOSPITAL MANAGEMENT SYSTEM</div>
            </div>
          </div>
        </div>

        {/* Headline */}
        <div style={{ position: "relative", zIndex: 1, marginBottom: 22 }}>
          <h1 style={{ fontFamily: spaceGrotesk, fontSize: 42, fontWeight: 700, color: "#fff", lineHeight: 1.15, letterSpacing: "-0.03em", marginBottom: 12, textAlign: "left" }}>
            Smarter care,<br /><span style={{ color: "#00C9A7" }}>seamless</span> workflow.
          </h1>
          <p style={{ color: "#7E9EC4", fontSize: 15, lineHeight: 1.7, maxWidth: 360, margin: 0, textAlign: "left" }}>
            A unified platform for every department — from clinical teams to administration, all working in sync.
          </p>
        </div>

        {/* Stat cards */}
        <div style={{ position: "relative", zIndex: 1, display: "flex", flexDirection: "column", gap: 10, width: "100%" }}>
          {[
            { label: "Active Patients Today",  value: "1,284",  trend: "+12%"       },
            { label: "Departments Connected",  value: "6 Roles",trend: "Integrated" },
            { label: "System Uptime",          value: "99.98%", trend: "Stable"     },
          ].map(s => (
            <div key={s.label} className="hc-stat">
              <div style={{ fontSize: 11, color: "#7E9EC4", letterSpacing: "0.05em", marginBottom: 3, fontWeight: 500, textTransform: "uppercase", textAlign: "left" }}>{s.label}</div>
              <div style={{ display: "flex", alignItems: "baseline", gap: 10 }}>
                <span style={{ fontFamily: spaceGrotesk, fontSize: 22, fontWeight: 700, color: "#fff" }}>{s.value}</span>
                <span style={{ fontSize: 12, color: "#00C9A7", fontWeight: 600, background: "rgba(0,201,167,0.12)", padding: "2px 8px", borderRadius: 20 }}>{s.trend}</span>
              </div>
            </div>
          ))}
        </div>

        <svg className="hc-ecg" viewBox="0 0 800 60" preserveAspectRatio="none">
          <path d="M0,30 L120,30 L140,30 L155,5 L170,55 L185,30 L200,30 L215,30 L230,15 L245,45 L260,30 L400,30 L420,30 L435,5 L450,55 L465,30 L480,30 L495,30 L510,15 L525,45 L540,30 L680,30 L700,30 L715,5 L730,55 L745,30 L760,30 L800,30"
            stroke="#00C9A7" strokeWidth="1.5" fill="none"/>
        </svg>
      </div>

      {/* ══ RIGHT PANEL ══ */}
      <div className="hc-right">
        {/* Inner wrapper: login centres itself, register starts from top */}
        <div className={`hc-right-inner${mode === "login" ? " login" : ""}`}>

          {/* Top badge */}
          <div style={{ display: "flex", alignItems: "center", marginBottom: 20 }}>
            <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
              <CrossIcon />
              <span style={{ fontFamily: spaceGrotesk, fontSize: 16, fontWeight: 700, color: "#0A1628" }}>HealthConnect</span>
            </div>
            <div style={{ marginLeft: "auto", display: "flex", alignItems: "center", gap: 6, background: "#E6FAF6", padding: "4px 10px", borderRadius: 20 }}>
              <div style={{ width: 7, height: 7, borderRadius: "50%", background: "#00C9A7", flexShrink: 0 }} />
              <span style={{ fontSize: 11, fontWeight: 600, color: "#00875A", letterSpacing: "0.04em" }}>SECURE SESSION</span>
            </div>
          </div>

          {/* Title */}
          <div style={{ marginBottom: 14 }}>
            <h2 style={{ fontFamily: spaceGrotesk, fontSize: 26, fontWeight: 700, color: "#0A1628", letterSpacing: "-0.02em", marginBottom: 4, textAlign: "left" }}>
              {mode === "login" ? "Welcome back" : "Create account"}
            </h2>
            <p style={{ fontSize: 14, color: "#7E8EA6", margin: 0, textAlign: "left" }}>
              {mode === "login" ? "Sign in to access your department portal" : "Register to get started with HealthConnect HMS"}
            </p>
          </div>

          {/* Tabs */}
          <div className="hc-tabs">
            <button className={`hc-tab${mode === "login"    ? " on" : ""}`} onClick={() => sw("login")}>Sign In</button>
            <button className={`hc-tab${mode === "register" ? " on" : ""}`} onClick={() => sw("register")}>Register</button>
          </div>

          {ok  && <div className="hc-ok" ><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><polyline points="20 6 9 17 4 12"/></svg>{ok}</div>}
          {err && <div className="hc-err"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>{err}</div>}

          {/* ── LOGIN FORM ── */}
          {mode === "login" && (
            <form onSubmit={login}>
              <Field icon={UserIcon} label="Username" value={lf.username} placeholder="Enter your username" onChange={e => setLf({...lf, username: e.target.value})} />
              <Field icon={LockIcon} label="Password" type={showPw ? "text" : "password"} value={lf.password} placeholder="Enter your password"
                onChange={e => setLf({...lf, password: e.target.value})}
                right={<span onClick={() => setShowPw(v => !v)}><EyeIcon open={showPw}/></span>} />
              <div style={{ display: "flex", justifyContent: "flex-end", marginBottom: 14, marginTop: -4 }}>
                <a href="#" style={{ fontSize: 13, color: "#00C9A7", fontWeight: 500, textDecoration: "none" }}>Forgot password?</a>
              </div>
              <button type="submit" className="hc-btn" disabled={loading}>{loading ? <span className="hc-spin"/> : "Sign In to Portal"}</button>
              <div style={{ textAlign: "center", marginTop: 14 }}>
                <span style={{ fontSize: 13, color: "#7E8EA6" }}>Don't have an account? </span>
                <button type="button" className="hc-lnk" onClick={() => sw("register")}>Register here</button>
              </div>
            </form>
          )}

          {/* ── REGISTER FORM ── */}
          {mode === "register" && (
            <form onSubmit={register}>
              <Field icon={UserIcon}   label="Username"      value={rf.username}     placeholder="Choose a username"    onChange={e => setRf({...rf, username: e.target.value})} />
              <Field icon={LockIcon}   label="Password"      type={showPw ? "text" : "password"} value={rf.password}   placeholder="Create a password"
                onChange={e => setRf({...rf, password: e.target.value})}
                right={<span onClick={() => setShowPw(v => !v)}><EyeIcon open={showPw}/></span>} />
              <Sel   icon={ShieldIcon} label="Role"          value={rf.role}         options={ROLES}                    onChange={e => setRf({...rf, role: e.target.value})} />
              <Field icon={MailIcon}   label="Email Address" type="email" value={rf.email} placeholder="your@hospital.com" onChange={e => setRf({...rf, email: e.target.value})} />
              <Field icon={PhoneIcon}  label="Phone Number"  type="tel"  value={rf.phoneNumber} placeholder="+91 XXXXX XXXXX" onChange={e => setRf({...rf, phoneNumber: e.target.value})} />
              <button type="submit" className="hc-btn" disabled={loading}>{loading ? <span className="hc-spin"/> : "Create Account"}</button>
              <div style={{ textAlign: "center", marginTop: 10 }}>
                <span style={{ fontSize: 13, color: "#7E8EA6" }}>Already registered? </span>
                <button type="button" className="hc-lnk" onClick={() => sw("login")}>Sign in</button>
              </div>
            </form>
          )}

          {/* Footer */}
          <div style={{ marginTop: 18, paddingTop: 14, borderTop: "1px solid #F0F5FF", display: "flex", justifyContent: "center", gap: 20 }}>
            {["Privacy Policy", "Terms of Use", "Support"].map(l => (
              <a key={l} href="#" style={{ fontSize: 12, color: "#B0BBC8", textDecoration: "none", fontWeight: 500 }}>{l}</a>
            ))}
          </div>
          <div style={{ textAlign: "center", marginTop: 8 }}>
            <span style={{ fontSize: 11, color: "#CBD5E0" }}>© 2026 HealthConnect HMS ·</span>
          </div>

        </div>{/* end hc-right-inner */}
      </div>
    </div>
  );
}