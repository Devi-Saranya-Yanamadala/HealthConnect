import { useState,useEffect} from "react";
import { useNavigate } from "react-router-dom";
import API from "../api";
/* ─── STYLES ──────────────────────────────────────────────── */
const GS = `
@import url('https://fonts.googleapis.com/css2?family=Syne:wght@600;700;800&family=DM+Sans:wght@400;500;600&display=swap');
*{box-sizing:border-box;margin:0;padding:0;}
body{font-family:'DM Sans',sans-serif;background:#F1F5F9;}
::-webkit-scrollbar{width:5px;}
::-webkit-scrollbar-thumb{background:#CBD5E1;border-radius:10px;}
@keyframes spin{to{transform:rotate(360deg)}}
@keyframes fadeIn{from{opacity:0;transform:translateY(10px)}to{opacity:1;transform:translateY(0)}}
.sbi{display:flex;align-items:center;gap:10px;padding:9px 10px;border-radius:10px;cursor:pointer;transition:all .18s;margin-bottom:3px;}
.sbi:hover{background:rgba(255,255,255,.08);}
.sbi.act{background:rgba(255,255,255,.14);}
.tbb{display:flex;align-items:center;justify-content:center;width:36px;height:36px;border-radius:9px;border:1px solid #E2E8F0;background:#fff;cursor:pointer;color:#475569;}
.tbb:hover{background:#F8FAFC;}
.page{animation:fadeIn .3s ease;}
`;

  /* ─── API ─────────────────────────────────────────────────── 
  const authHeaders = () => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
      localStorage.clear();
      window.location.href = "/login";
      return {};
    }

    return {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`
    };
  };
*/

/* ─── ICON ────────────────────────────────────────────────── */
const Ic = ({d,size=18}) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none"
    stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
    <path d={d}/>
  </svg>
);

const D = {
  search:  "M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z",
  plus:    "M12 4v16m8-8H4",
  list:    "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2",
  check:   "M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z",
  x:       "M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z",
  power:   "M18.364 5.636a9 9 0 010 12.728M15.536 8.464a5 5 0 010 7.072M12 12h.01",
  cal:     "M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z",
  clock:   "M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z",
  lock:    "M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z",
  book:    "M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253",
  rel:     "M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4",
  skull:   "M12 2C8.13 2 5 5.13 5 9c0 2.38 1.19 4.47 3 5.74V17a1 1 0 001 1h6a1 1 0 001-1v-2.26C17.81 13.47 19 11.38 19 9c0-3.87-3.13-7-7-7zM9 21h6",
  refresh: "M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15",
  menu:    "M4 6h16M4 12h16M4 18h16",
  logout:  "M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1",
  home:    "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6",
  bell:    "M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9",
  chev:    "M9 5l7 7-7 7",
  cancel:  "M6 18L18 6M6 6l12 12",
  swap:    "M7 16V4m0 0L3 8m4-4l4 4m6 0v12m0 0l4-4m-4 4l-4-4",
  dl:      "M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4",
  chart:   "M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z",
  kpi:     "M13 7h8m0 0v8m0-8l-8 8-4-4-6 6",
  bed:     "M3 9h18M3 9V6a1 1 0 011-1h16a1 1 0 011 1v3M3 9v9a1 1 0 001 1h16a1 1 0 001-1V9",
  admit:   "M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z",
  disc:    "M17 16l4-4m0 0l-4-4m4 4H7",
  ward:    "M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4",
  money:   "M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z",
  shield:  "M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z",
  users:   "M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z",
  notif:   "M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9",
  mail:    "M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z",
  doc:     "M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z",
  log:     "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4",
};

/* ─── BASE COMPONENTS ─────────────────────────────────────── */
const IS = {width:"100%",padding:"10px 13px",background:"#F8FAFC",border:"1.5px solid #E2E8F0",borderRadius:9,fontSize:13,color:"#1E293B",outline:"none",fontFamily:"'DM Sans',sans-serif",boxSizing:"border-box",transition:"border .15s"};

function FInput({icon,...p}){
  const [f,sf]=useState(false);
  return(
    <div style={{position:"relative"}}>
      {icon&&<div style={{position:"absolute",left:11,top:"50%",transform:"translateY(-50%)",color:f?"#3B82F6":"#94A3B8",display:"flex",alignItems:"center",pointerEvents:"none"}}><Ic d={icon} size={15}/></div>}
      <input {...p} style={{...IS,paddingLeft:icon?36:13,border:`1.5px solid ${f?"#3B82F6":"#E2E8F0"}`,boxShadow:f?"0 0 0 3px rgba(59,130,246,.1)":"none",background:f?"#fff":"#F8FAFC",...(p.style||{})}} onFocus={()=>sf(true)} onBlur={()=>sf(false)}/>
    </div>
  );
}

function FSel({children,...p}){
  const [f,sf]=useState(false);
  return(
    <div style={{position:"relative"}}>
      <select {...p} style={{...IS,appearance:"none",cursor:"pointer",border:`1.5px solid ${f?"#3B82F6":"#E2E8F0"}`,boxShadow:f?"0 0 0 3px rgba(59,130,246,.1)":"none",background:f?"#fff":"#F8FAFC",color:p.value?"#1E293B":"#94A3B8",...(p.style||{})}} onFocus={()=>sf(true)} onBlur={()=>sf(false)}>{children}</select>
      <div style={{position:"absolute",right:11,top:"50%",transform:"translateY(-50%)",pointerEvents:"none",color:"#94A3B8"}}><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><polyline points="6 9 12 15 18 9"/></svg></div>
    </div>
  );
}

const Fld=({label,required,children})=>(
  <div style={{marginBottom:16}}>
    <label style={{display:"block",fontSize:11,fontWeight:700,letterSpacing:"0.07em",textTransform:"uppercase",color:"#64748B",marginBottom:6}}>
      {label}{required&&<span style={{color:"#EF4444",marginLeft:3}}>*</span>}
    </label>
    {children}
  </div>
);

function Btn({variant="primary",color="#3B82F6",icon,loading,children,style:xs={},...p}){
  const ip=variant==="primary";
  return(
    <button {...p} style={{display:"inline-flex",alignItems:"center",gap:7,padding:"10px 18px",borderRadius:9,fontSize:13,fontWeight:600,cursor:p.disabled?"not-allowed":"pointer",fontFamily:"'DM Sans',sans-serif",transition:"all .15s",border:`1.5px solid ${ip?color:"#E2E8F0"}`,background:ip?color:"#fff",color:ip?"#fff":"#475569",opacity:p.disabled?.6:1,...xs}}>
      {loading?<span style={{width:14,height:14,border:"2px solid rgba(255,255,255,.4)",borderTop:"2px solid #fff",borderRadius:"50%",animation:"spin .6s linear infinite",display:"inline-block"}}/>:icon&&<Ic d={icon} size={15}/>}
      {children}
    </button>
  );
}

const BM={ACTIVE:{bg:"#DCFCE7",c:"#16A34A",l:"Active"},INACTIVE:{bg:"#FEF9C3",c:"#CA8A04",l:"Inactive"},DECEASED:{bg:"#F3F4F6",c:"#6B7280",l:"Deceased"},AVAILABLE:{bg:"#DBEAFE",c:"#2563EB",l:"Available"},BOOKED:{bg:"#FEE2E2",c:"#DC2626",l:"Booked"},SCHEDULED:{bg:"#DBEAFE",c:"#2563EB",l:"Scheduled"},CANCELLED:{bg:"#FEE2E2",c:"#DC2626",l:"Cancelled"},COMPLETED:{bg:"#DCFCE7",c:"#16A34A",l:"Completed"},ADMITTED:{bg:"#FEF9C3",c:"#D97706",l:"Admitted"},DISCHARGED:{bg:"#F3F4F6",c:"#6B7280",l:"Discharged"},PAID:{bg:"#DCFCE7",c:"#16A34A",l:"Paid"},UNPAID:{bg:"#FEE2E2",c:"#DC2626",l:"Unpaid"},PARTIAL:{bg:"#FEF9C3",c:"#D97706",l:"Partial"},APPROVED:{bg:"#DCFCE7",c:"#16A34A",l:"Approved"},REJECTED:{bg:"#FEE2E2",c:"#DC2626",l:"Rejected"},SETTLED:{bg:"#DBEAFE",c:"#2563EB",l:"Settled"},PENDING:{bg:"#FEF9C3",c:"#D97706",l:"Pending"},OCCUPIED:{bg:"#FEE2E2",c:"#DC2626",l:"Occupied"},};
const Badge=({status})=>{const s=BM[status]||{bg:"#F1F5F9",c:"#64748B",l:status||"—"};return <span style={{background:s.bg,color:s.c,fontSize:11,fontWeight:700,padding:"3px 10px",borderRadius:20,letterSpacing:"0.04em"}}>{s.l}</span>;};

function Toast({msg,type,onClose}){
  if(!msg)return null;
  const ok=type==="success";
  return(
    <div style={{position:"fixed",bottom:28,right:28,zIndex:9999,background:ok?"#ECFDF5":"#FEF2F2",border:`1px solid ${ok?"#BBF7D0":"#FECACA"}`,color:ok?"#065F46":"#991B1B",borderRadius:12,padding:"14px 18px",display:"flex",alignItems:"center",gap:10,fontSize:13,fontWeight:600,boxShadow:"0 8px 24px rgba(0,0,0,.1)",minWidth:260,animation:"fadeIn .25s ease"}}>
      <Ic d={ok?D.check:D.x} size={16}/><span style={{flex:1}}>{msg}</span><span style={{cursor:"pointer",opacity:.6}} onClick={onClose}>✕</span>
    </div>
  );
}

function Card({title,icon,color="#3B82F6",children,action}){
  return(
    <div style={{background:"#fff",borderRadius:14,border:"1px solid #E2E8F0",marginBottom:20,boxShadow:"0 1px 4px rgba(0,0,0,.04)",overflow:"hidden"}}>
      <div style={{display:"flex",alignItems:"center",gap:10,padding:"15px 20px",borderBottom:"1px solid #F1F5F9"}}>
        <div style={{width:32,height:32,borderRadius:9,background:`${color}18`,display:"flex",alignItems:"center",justifyContent:"center",color}}><Ic d={icon} size={16}/></div>
        <span style={{fontFamily:"'Syne',sans-serif",fontSize:15,fontWeight:700,color:"#1E293B",flex:1}}>{title}</span>
        {action}
      </div>
      <div style={{padding:20}}>{children}</div>
    </div>
  );
}

function DT({cols,rows}){
  return(
    <div style={{overflowX:"auto",borderRadius:10,border:"1px solid #E2E8F0"}}>
      <table style={{width:"100%",borderCollapse:"collapse",fontSize:13,fontFamily:"'DM Sans',sans-serif"}}>
        <thead><tr style={{background:"#F8FAFC"}}>{cols.map(c=><th key={c.k} style={{padding:"10px 14px",textAlign:"left",fontSize:11,fontWeight:700,letterSpacing:"0.06em",textTransform:"uppercase",color:"#64748B",borderBottom:"1px solid #E2E8F0",whiteSpace:"nowrap"}}>{c.l}</th>)}</tr></thead>
        <tbody>
          {rows.length===0
            ?<tr><td colSpan={cols.length} style={{textAlign:"center",padding:32,color:"#94A3B8",fontStyle:"italic"}}>No records found</td></tr>
            :rows.map((row,i)=><tr key={i} style={{borderBottom:"1px solid #F1F5F9",background:i%2===0?"#fff":"#FAFAFA"}}>{cols.map(c=><td key={c.k} style={{padding:"11px 14px",color:"#334155",verticalAlign:"middle"}}>{c.r?c.r(row):(row[c.k]??"—")}</td>)}</tr>)}
        </tbody>
      </table>
    </div>
  );
}

function Tabs({tabs,active,setActive,color}){
  return(
    <div style={{display:"flex",gap:6,background:"#F1F5F9",borderRadius:12,padding:5,marginBottom:24,flexWrap:"wrap",width:"fit-content"}}>
      {tabs.map(t=>(
        <button key={t.id} onClick={()=>setActive(t.id)} style={{display:"flex",alignItems:"center",gap:7,padding:"9px 18px",borderRadius:9,border:"none",background:active===t.id?"#fff":"transparent",color:active===t.id?color:"#64748B",fontSize:13,fontWeight:600,cursor:"pointer",fontFamily:"'DM Sans',sans-serif",boxShadow:active===t.id?"0 1px 6px rgba(0,0,0,.09)":"none",transition:"all .18s"}}>
          <Ic d={t.icon} size={15}/>{t.label}
        </button>
      ))}
    </div>
  );
}

function PH({icon,title,sub,bg,color}){
  return(
    <div style={{display:"flex",alignItems:"center",gap:14,marginBottom:24}}>
      <div style={{width:48,height:48,borderRadius:14,background:bg,display:"flex",alignItems:"center",justifyContent:"center",color,flexShrink:0}}><Ic d={icon} size={24}/></div>
      <div><h1 style={{fontFamily:"'Syne',sans-serif",fontSize:21,fontWeight:800,color:"#0F172A",margin:0}}>{title}</h1><p style={{fontSize:13,color:"#64748B",margin:0}}>{sub}</p></div>
    </div>
  );
}

function Empty({emoji,label,hint}){
  return(<div style={{textAlign:"center",padding:"40px 20px",color:"#94A3B8"}}><div style={{fontSize:36,marginBottom:12}}>{emoji}</div><div style={{fontWeight:600,marginBottom:6,color:"#64748B",fontSize:14}}>{label}</div><div style={{fontSize:13}}>{hint}</div></div>);
}

function useToast(){
  const [toast,setToast]=useState(null);
  const notify=(msg,type="success")=>{setToast({msg,type});setTimeout(()=>setToast(null),3500);};
  return [toast,notify,setToast];
}

/* ─── DOCTOR MODULE ───────────────────────────────────────── */
const SPECS = [
  "Cardiology","Neurology","Orthopedics","Pediatrics","General Surgery",
  "Dermatology","Ophthalmology","ENT","Radiology","Oncology",
  "Psychiatry","Gynecology","Urology","Endocrinology","Pulmonology"
];

const DEPTS = [
  "Emergency","ICU","OPD","Surgery","Radiology",
  "Pediatrics","Cardiology","Neurology","Oncology","General Ward"
];

const D0 = {
  doctorCode:"", fullName:"", specialization:"", department:"",
  licenseNumber:"", phoneNumber:"", email:"",
  workingStartTime:"", workingEndTime:""
};

/* ─── RESPONSE CARD ───────────────────────────────────────── */
function ResponseCard({title, data, color="#059669", onClose}) {
  if (!data) return null;
  const rows = Object.entries(data).filter(([,v])=>v!=null);
  return (
    <div style={{marginTop:20,borderRadius:12,border:`1.5px solid ${color}40`,
      background:`${color}08`,overflow:"hidden",animation:"fadeIn .3s ease"}}>
      <div style={{display:"flex",alignItems:"center",gap:10,
        padding:"12px 16px",background:`${color}12`,
        borderBottom:`1px solid ${color}30`}}>
        <div style={{width:28,height:28,borderRadius:8,background:`${color}20`,
          display:"flex",alignItems:"center",justifyContent:"center",color}}>
          <Ic d={D.check} size={14}/>
        </div>
        <span style={{fontFamily:"'Syne',sans-serif",fontSize:13,fontWeight:700,
          color,flex:1}}>{title}</span>
        <span style={{cursor:"pointer",color,opacity:.6,fontSize:16,lineHeight:1}}
          onClick={onClose}>✕</span>
      </div>
      <div style={{padding:"14px 16px"}}>
        <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"10px 24px"}}>
          {rows.map(([k,v])=>(
            <div key={k} style={{display:"flex",flexDirection:"column",gap:3}}>
              <span style={{fontSize:10,fontWeight:700,letterSpacing:"0.06em",
                textTransform:"uppercase",color:"#94A3B8"}}>
                {k.replace(/([A-Z])/g," $1").replace(/^./,s=>s.toUpperCase())}
              </span>
              <span style={{fontSize:13,fontWeight:600,color:"#1E293B",wordBreak:"break-all"}}>
                {String(v)}
              </span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function DoctorModule() {
  const [tab, st]   = useState("create");
  const [form, sf]  = useState(D0);
  const [loading, setLoading] = useState(false);
  const [toast, notify, setToast] = useToast();

  /* create response */
  const [createRes, setCreateRes] = useState(null);

  /* retrieve */
  const [fetchCode, setFetchCode] = useState("");
  const [doctor, setDoctor]       = useState(null);
  const [fetchLoading, setFetchLoading] = useState(false);

  /* all doctors */
  const [allDoctors, setAllDoctors]   = useState([]);
  const [allLoading, setAllLoading]   = useState(false);

  /* status */
  const [statusCode, setStatusCode]     = useState("");
  const [statusAction, setStatusAction] = useState("ACTIVE");
  const [statusLoading, setStatusLoading] = useState(false);

  const onChange = key => e => sf(prev=>({...prev,[key]:e.target.value}));

  /* ─── CREATE DOCTOR ─── */
  const create = async () => {
    if (!form.doctorCode||!form.fullName||!form.specialization||
        !form.department||!form.licenseNumber||!form.phoneNumber||
        !form.email||!form.workingStartTime||!form.workingEndTime)
      return notify("Fill all required fields","error");

    setLoading(true);
    setCreateRes(null);
    try {
      const res = await API.post("/doctors", form);
      setCreateRes(res.data);          // ← capture full response
      notify("Doctor created!");
      sf(D0);
    } catch(e) {
      notify(e?.response?.data?.message || e.message || "Creation failed","error");
    } finally { setLoading(false); }
  };

  /* ─── RETRIEVE BY CODE ─── */
  const retrieve = async () => {
    if (!fetchCode.trim()) return notify("Enter doctor code","error");
    setFetchLoading(true);
    setDoctor(null);
    try {
      const res = await API.get(`/doctors/${fetchCode}`);
      setDoctor(res.data);
    } catch { notify("Doctor not found","error"); }
    finally { setFetchLoading(false); }
  };

  /* ─── GET ALL ─── */
  const getAll = async () => {
    setAllLoading(true);
    try {
      const res = await API.get("/doctors");
      setAllDoctors(Array.isArray(res.data) ? res.data : []);
    } catch { notify("Failed to load doctors","error"); }
    finally { setAllLoading(false); }
  };

  /* ─── ACTIVATE / DEACTIVATE ─── */
  const changeStatus = async () => {
    if (!statusCode.trim()) return notify("Enter doctor code","error");
    setStatusLoading(true);
    try {
      const action = statusAction==="ACTIVE" ? "activate" : "deactivate";
      await API.put(`/doctors/${statusCode}/${action}`, {});
      notify(`Doctor ${statusCode} → ${statusAction}`);
      setStatusCode("");
    } catch(e) { notify(e.message||"Status update failed","error"); }
    finally { setStatusLoading(false); }
  };

  const TABS = [
    {id:"create",   label:"Create",      icon:D.plus},
    {id:"retrieve", label:"Retrieve",    icon:D.search},
    {id:"all",      label:"All Doctors", icon:D.list},
  ];

  return (
    <div style={{padding:28}}>
      <PH
        icon="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
        title="Doctor Services"
        sub="Create, retrieve and manage doctors"
        bg="#EFF6FF" color="#3B82F6"
      />
      <Tabs tabs={TABS} active={tab} setActive={st} color="#3B82F6"/>

      {/* ─── CREATE ─── */}
      {tab==="create" && (
        <Card title="Create Doctor Record" icon={D.plus} color="#3B82F6">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Doctor Code" required>
              <FInput value={form.doctorCode} onChange={onChange("doctorCode")}
                placeholder="DOC-001"/>
            </Fld>
            <Fld label="Full Name" required>
              <FInput value={form.fullName} onChange={onChange("fullName")}
                placeholder="Dr. John Smith"/>
            </Fld>
            <Fld label="Specialization" required>
              <FSel value={form.specialization} onChange={onChange("specialization")}>
                <option value="">Select</option>
                {SPECS.map(s=><option key={s}>{s}</option>)}
              </FSel>
            </Fld>
            <Fld label="Department" required>
              <FSel value={form.department} onChange={onChange("department")}>
                <option value="">Select</option>
                {DEPTS.map(d=><option key={d}>{d}</option>)}
              </FSel>
            </Fld>
            <Fld label="License Number" required>
              <FInput value={form.licenseNumber} onChange={onChange("licenseNumber")}
                placeholder="LIC-12345"/>
            </Fld>
            <Fld label="Phone Number" required>
              <FInput value={form.phoneNumber} onChange={onChange("phoneNumber")}
                placeholder="9876543210"/>
            </Fld>
            <Fld label="Email" required>
              <FInput value={form.email} onChange={onChange("email")}
                placeholder="doctor@hospital.com" type="email"/>
            </Fld>
            <div/>
            <Fld label="Working Start Time" required>
              <FInput value={form.workingStartTime}
                onChange={onChange("workingStartTime")} type="time"/>
            </Fld>
            <Fld label="Working End Time" required>
              <FInput value={form.workingEndTime}
                onChange={onChange("workingEndTime")} type="time"/>
            </Fld>
          </div>

          <div style={{display:"flex",gap:10,marginTop:8}}>
            <Btn color="#3B82F6" icon={D.plus} loading={loading} onClick={create}>
              Create Doctor
            </Btn>
            <Btn variant="secondary" onClick={()=>{sf(D0);setCreateRes(null);}}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE CARD — shows backend JSON just like Swagger ── */}
          <ResponseCard
            title="Doctor Created Successfully"
            data={createRes}
            color="#3B82F6"
            onClose={()=>setCreateRes(null)}
          />
        </Card>
      )}

      {/* ─── RETRIEVE ─── */}
      {tab==="retrieve" && (
        <>
          <Card title="Retrieve Doctor by Code" icon={D.search} color="#3B82F6">
            <Fld label="Doctor Code">
              <FInput value={fetchCode}
                onChange={e=>setFetchCode(e.target.value)}
                onKeyDown={e=>e.key==="Enter"&&retrieve()}
                placeholder="DOC-001"/>
            </Fld>
            <Btn color="#3B82F6" icon={D.search} loading={fetchLoading} onClick={retrieve}>
              Retrieve
            </Btn>

            {/* ── Shows full doctor record just like Swagger ── */}
            {doctor && (
              <ResponseCard
                title="Doctor Details"
                data={doctor}
                color="#3B82F6"
                onClose={()=>setDoctor(null)}
              />
            )}
          </Card>

          <Card title="Activate / Deactivate" icon={D.power} color="#F59E0B">
            <Fld label="Doctor Code">
              <FInput value={statusCode}
                onChange={e=>setStatusCode(e.target.value)}
                placeholder="DOC-001"/>
            </Fld>
            <Fld label="Action">
              <FSel value={statusAction} onChange={e=>setStatusAction(e.target.value)}>
                <option value="ACTIVE">Activate</option>
                <option value="INACTIVE">Deactivate</option>
              </FSel>
            </Fld>
            <Btn
              color={statusAction==="ACTIVE"?"#059669":"#EF4444"}
              loading={statusLoading}
              onClick={changeStatus}>
              Process
            </Btn>
          </Card>
        </>
      )}

      {/* ─── ALL DOCTORS ─── */}
      {tab==="all" && (
        <Card title="All Doctors" icon={D.list} color="#3B82F6"
          action={
            <Btn color="#3B82F6" icon={D.refresh} loading={allLoading} onClick={getAll}>
              {allDoctors.length ? "Refresh" : "Load All"}
            </Btn>
          }>
          {allDoctors.length===0
            ? <Empty emoji="👨‍⚕️" label="No doctors loaded"
                hint="Click Load All to fetch doctors"/>
            : <DT rows={allDoctors} cols={[
                {k:"doctorCode",     l:"Code"},
                {k:"fullName",       l:"Name"},
                {k:"specialization", l:"Specialization"},
                {k:"department",     l:"Department"},
                {k:"active",         l:"Status",
                  r:r=><Badge status={r.active===false?"INACTIVE":"ACTIVE"}/>},
              ]}/>
          }
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}

/* ─── PATIENT MODULE ──────────────────────────────────────── */
const BLOOD = [
  {label:"A+",  value:"A_POS"},
  {label:"A-",  value:"A_NEG"},
  {label:"B+",  value:"B_POS"},
  {label:"B-",  value:"B_NEG"},
  {label:"AB+", value:"AB_POS"},
  {label:"AB-", value:"AB_NEG"},
  {label:"O+",  value:"O_POS"},
  {label:"O-",  value:"O_NEG"},
];

const P0 = {
  patientCode:"", fullName:"", gender:"", dob:"", phone:"",
  email:"", bloodGroup:"", address:"",
  emergencyContactName:"", emergencyContactPhone:"", nationalId:"",
};

function PatientModule() {
  const [tab, setTab]   = useState("register");
  const [form, setForm] = useState(P0);
  const [loading, setLoading]     = useState(false);
  const [toast, notify, setToast] = useToast();

  const [createRes, setCreateRes] = useState(null);   // ← register response

  const [searchCode, setSearchCode] = useState("");
  const [patient, setPatient]       = useState(null);
  const [getLoading, setGetLoading] = useState(false);

  const [patientCode, setPatientCode] = useState("");
  const [action, setAction]           = useState("INACTIVE");
  const [statusLoading, setStatusLoading] = useState(false);
  const [statusRes, setStatusRes]     = useState(null);  // ← status response

  const handleChange = key => e => setForm(prev=>({...prev,[key]:e.target.value}));

  /* ─── REGISTER ─── */
  const reg = async () => {
    if (!form.patientCode||!form.fullName||!form.gender||!form.dob||
        !form.phone||!form.bloodGroup||!form.address||
        !form.emergencyContactName||!form.emergencyContactPhone||!form.nationalId)
      return notify("Fill all required fields","error");

    if (!/^[6-9][0-9]{9}$/.test(form.phone))
      return notify("Phone must be 10-digit Indian number (e.g. 9876543210)","error");

    if (!/^[6-9][0-9]{9}$/.test(form.emergencyContactPhone))
      return notify("Emergency phone must be 10-digit Indian number","error");

    setLoading(true);
    setCreateRes(null);
    try {
      const payload = {
        patientCode:           form.patientCode,
        fullName:              form.fullName,
        gender:                form.gender,
        dob:                   form.dob,
        phone:                 form.phone,
        email:                 form.email||null,
        bloodGroup:            form.bloodGroup,
        address:               form.address,
        emergencyContactName:  form.emergencyContactName,
        emergencyContactPhone: form.emergencyContactPhone,
        nationalId:            form.nationalId,
      };
      const res = await API.post("/patients", payload);
      setCreateRes(res.data);               // ← capture response
      notify("Patient registered successfully!");
      setForm(P0);
    } catch(e) {
      const msg = e?.response?.data?.message
               || Object.values(e?.response?.data||{}).join(", ")
               || e.message || "Registration failed";
      notify(msg,"error");
    } finally { setLoading(false); }
  };

  /* ─── RETRIEVE ─── */
  const getByCode = async () => {
    if (!searchCode.trim()) return notify("Enter patient code","error");
    setGetLoading(true);
    setPatient(null);
    try {
      const res = await API.get(`/patients/${searchCode}`);
      setPatient(res.data);
    } catch { notify("Patient not found","error"); }
    finally { setGetLoading(false); }
  };

  /* ─── STATUS ─── */
  const chgSt = async () => {
    if (!patientCode.trim()) return notify("Enter patient code","error");
    setStatusLoading(true);
    setStatusRes(null);
    try {
      const endpoint = {
        INACTIVE: `/patients/${patientCode}/deactivate`,
        DECEASED: `/patients/${patientCode}/deceased`,
      }[action];
      if (!endpoint) return notify("Action not supported","error");
      const res = await API.put(endpoint, {});
      setStatusRes(res.data);               // ← capture response
      notify(`Patient ${patientCode} → ${action}`);
      setPatientCode("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Status update failed";
      notify(msg,"error");
    } finally { setStatusLoading(false); }
  };

  const TABS = [
    {id:"register", label:"Register",      icon:D.plus},
    {id:"retrieve", label:"Retrieve",      icon:D.search},
    {id:"status",   label:"Update Status", icon:D.power},
  ];

  return (
    <div style={{padding:28}}>
      <PH icon={D.patient} title="Patient Management"
        sub="Register patients and manage status"
        bg="#F5F3FF" color="#8B5CF6"/>
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#8B5CF6"/>

      {/* ─── REGISTER ─── */}
      {tab==="register" && (
        <Card title="Register New Patient" icon={D.plus} color="#8B5CF6">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Patient Code" required>
              <FInput placeholder="PAT-001" value={form.patientCode}
                onChange={handleChange("patientCode")}/>
            </Fld>
            <Fld label="Full Name" required>
              <FInput placeholder="John Doe" value={form.fullName}
                onChange={handleChange("fullName")}/>
            </Fld>
            <Fld label="Gender" required>
              <FSel value={form.gender} onChange={handleChange("gender")}>
                <option value="">Select</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </FSel>
            </Fld>
            <Fld label="Date of Birth" required>
              <FInput type="date" value={form.dob} onChange={handleChange("dob")}/>
            </Fld>
            <Fld label="Phone" required>
              <FInput placeholder="9876543210 (10 digits, no +91)"
                value={form.phone} onChange={handleChange("phone")}/>
            </Fld>
            <Fld label="Email">
              <FInput placeholder="patient@email.com" value={form.email}
                onChange={handleChange("email")}/>
            </Fld>
            <Fld label="Blood Group" required>
              <FSel value={form.bloodGroup} onChange={handleChange("bloodGroup")}>
                <option value="">Select</option>
                {BLOOD.map(b=>(
                  <option key={b.value} value={b.value}>{b.label}</option>
                ))}
              </FSel>
            </Fld>
            <Fld label="National ID" required>
              <FInput placeholder="Aadhaar / PAN / Passport"
                value={form.nationalId} onChange={handleChange("nationalId")}/>
            </Fld>
            <Fld label="Address" required>
              <FInput placeholder="Street, City, PIN"
                value={form.address} onChange={handleChange("address")}/>
            </Fld>
            <div/>
            <Fld label="Emergency Contact Name" required>
              <FInput placeholder="Jane Doe"
                value={form.emergencyContactName}
                onChange={handleChange("emergencyContactName")}/>
            </Fld>
            <Fld label="Emergency Contact Phone" required>
              <FInput placeholder="9123456789 (10 digits, no +91)"
                value={form.emergencyContactPhone}
                onChange={handleChange("emergencyContactPhone")}/>
            </Fld>
          </div>

          <div style={{display:"flex",gap:10,marginTop:8}}>
            <Btn color="#8B5CF6" loading={loading} onClick={reg} icon={D.plus}>
              Register Patient
            </Btn>
            <Btn variant="secondary" onClick={()=>{setForm(P0);setCreateRes(null);}}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — shows registered patient details like Swagger ── */}
          <ResponseCard
            title="Patient Registered Successfully"
            data={createRes}
            color="#8B5CF6"
            onClose={()=>setCreateRes(null)}
          />
        </Card>
      )}

      {/* ─── RETRIEVE ─── */}
      {tab==="retrieve" && (
        <Card title="Retrieve Patient by Code" icon={D.search} color="#8B5CF6">
          <Fld label="Patient Code">
            <FInput placeholder="PAT-001" value={searchCode}
              onChange={e=>setSearchCode(e.target.value)}
              onKeyDown={e=>e.key==="Enter"&&getByCode()}/>
          </Fld>
          <Btn color="#8B5CF6" loading={getLoading} onClick={getByCode}>
            Retrieve Patient
          </Btn>

          {/* ── RESPONSE — full patient record like Swagger ── */}
          <ResponseCard
            title="Patient Details"
            data={patient}
            color="#8B5CF6"
            onClose={()=>setPatient(null)}
          />
        </Card>
      )}

      {/* ─── STATUS ─── */}
      {tab==="status" && (
        <Card title="Update Patient Status" icon={D.power} color="#8B5CF6">
          <Fld label="Patient Code">
            <FInput placeholder="PAT-001" value={patientCode}
              onChange={e=>setPatientCode(e.target.value)}/>
          </Fld>
          <Fld label="Action">
            <FSel value={action} onChange={e=>setAction(e.target.value)}>
              <option value="INACTIVE">Deactivate</option>
              <option value="DECEASED">Mark as Deceased</option>
            </FSel>
          </Fld>
          <Btn color="#8B5CF6" loading={statusLoading} onClick={chgSt}>
            Process
          </Btn>

          {/* ── RESPONSE — updated patient record ── */}
          <ResponseCard
            title="Status Updated"
            data={statusRes}
            color="#8B5CF6"
            onClose={()=>setStatusRes(null)}
          />
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── SLOT MODULE ─────────────────────────────────────────── */
function SlotModule() {
  const [tab, setTab] = useState("generate");
  const [toast, notify, setToast] = useToast();

  /* generate */
  const [genLoading, setGenLoading] = useState(false);
  const [genRes, setGenRes]         = useState(null);  // ← generate confirmation
  const [form, setForm] = useState({
    doctorCode:"", slotDate:"", startTime:"", endTime:"", slotDuration:""
  });

  /* view */
  const [getLoading, setGetLoading] = useState(false);
  const [doctorCode, setDoctorCode] = useState("");
  const [date, setDate]             = useState("");
  const [slots, setSlots]           = useState([]);

  /* book / release */
  const [bookId, setBookId]         = useState("");
  const [bookLoading, setBookLoading]   = useState(false);
  const [bookRes, setBookRes]           = useState(null);  // ← booked slot info

  const [releaseId, setReleaseId]       = useState("");
  const [releaseLoading, setReleaseLoading] = useState(false);
  const [releaseRes, setReleaseRes]         = useState(null); // ← released slot info

  const onFormChange = key => e => setForm(prev=>({...prev,[key]:e.target.value}));

  /* ─── GENERATE ─── */
  const generate = async () => {
    const {doctorCode, slotDate, startTime, endTime, slotDuration} = form;
    if (!doctorCode||!slotDate||!startTime||!endTime||!slotDuration)
      return notify("Fill all fields","error");

    setGenLoading(true);
    setGenRes(null);
    try {
      // startTime/endTime are LocalTime — send null if empty to avoid 500
      await API.post("/slots/generate", {
        doctorCode,
        slotDate,
        startTime: startTime || null,
        endTime:   endTime   || null,
        slotDuration: Number(slotDuration),
      });
      // generate returns Void — build a confirmation object to display
      setGenRes({
        doctorCode,
        slotDate,
        startTime,
        endTime,
        slotDuration: `${slotDuration} mins`,
        result: "Slots generated successfully",
      });
      notify(`Slots generated for ${doctorCode}`);
      setForm({doctorCode:"",slotDate:"",startTime:"",endTime:"",slotDuration:""});
    } catch(e) {
      const msg = e?.response?.data?.message
               || Object.values(e?.response?.data||{}).join(", ")
               || e.message || "Generation failed";
      notify(msg,"error");
    } finally { setGenLoading(false); }
  };

  /* ─── GET SLOTS ─── */
  const getSlots = async () => {
    if (!doctorCode||!date) return notify("Enter doctor code and date","error");
    setGetLoading(true);
    setSlots([]);
    try {
      const res = await API.get(`/slots?doctorCode=${doctorCode}&date=${date}`);
      setSlots(Array.isArray(res.data) ? res.data : []);
      if ((res.data||[]).length===0) notify("No slots found for this date","error");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Failed to fetch slots";
      notify(msg,"error");
    } finally { setGetLoading(false); }
  };

  /* ─── BOOK SLOT ─── */
  const bookSlot = async () => {
    if (!bookId.trim()) return notify("Enter slot ID","error");
    setBookLoading(true);
    setBookRes(null);
    try {
      await API.put(`/slots/book/${bookId}`, {});
      // book returns Void — show the slot details from current loaded slots if available
      const matched = slots.find(s=>String(s.slotId)===String(bookId));
      setBookRes({
        slotId:    bookId,
        startTime: matched?.startTime || "—",
        endTime:   matched?.endTime   || "—",
        status:    "BOOKED",
        result:    "Slot booked successfully",
      });
      notify(`Slot ${bookId} booked`);
      setBookId("");
      // update slot status in view table if loaded
      setSlots(prev=>prev.map(s=>
        String(s.slotId)===String(bookId) ? {...s, status:"BOOKED"} : s
      ));
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Booking failed";
      notify(msg,"error");
    } finally { setBookLoading(false); }
  };

  /* ─── RELEASE SLOT ─── */
  const releaseSlot = async () => {
    if (!releaseId.trim()) return notify("Enter slot ID","error");
    setReleaseLoading(true);
    setReleaseRes(null);
    try {
      await API.patch(`/slots/${releaseId}/release`, {});
      const matched = slots.find(s=>String(s.slotId)===String(releaseId));
      setReleaseRes({
        slotId:    releaseId,
        startTime: matched?.startTime || "—",
        endTime:   matched?.endTime   || "—",
        status:    "AVAILABLE",
        result:    "Slot released successfully",
      });
      notify(`Slot ${releaseId} released`);
      setReleaseId("");
      setSlots(prev=>prev.map(s=>
        String(s.slotId)===String(releaseId) ? {...s, status:"AVAILABLE"} : s
      ));
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Release failed";
      notify(msg,"error");
    } finally { setReleaseLoading(false); }
  };

  const TABS = [
    {id:"generate", label:"Generate",     icon:D.plus},
    {id:"view",     label:"View Slots",   icon:D.cal},
    {id:"manage",   label:"Book/Release", icon:D.book},
  ];

  return (
    <div style={{padding:28}}>
      <PH
        icon="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
        title="Slot Services"
        sub="Generate, view, book and release slots"
        bg="#ECFEFF" color="#0891B2"
      />
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#0891B2"/>

      {/* ─── GENERATE ─── */}
      {tab==="generate" && (
        <Card title="Generate Slots" icon={D.plus} color="#0891B2">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Doctor Code" required>
              <FInput value={form.doctorCode} onChange={onFormChange("doctorCode")}
                placeholder="DOC-001" icon={D.search}/>
            </Fld>
            <Fld label="Slot Date" required>
              <FInput value={form.slotDate} onChange={onFormChange("slotDate")}
                type="date" icon={D.cal}/>
            </Fld>
            <Fld label="Start Time" required>
              <FInput value={form.startTime} onChange={onFormChange("startTime")}
                type="time" icon={D.clock}/>
            </Fld>
            <Fld label="End Time" required>
              <FInput value={form.endTime} onChange={onFormChange("endTime")}
                type="time" icon={D.clock}/>
            </Fld>
            <Fld label="Duration (mins)" required>
              <FSel value={form.slotDuration} onChange={onFormChange("slotDuration")}>
                <option value="">Select</option>
                {["15","20","30","45","60"].map(d=>(
                  <option key={d} value={d}>{d}</option>
                ))}
              </FSel>
            </Fld>
          </div>

          <div style={{display:"flex",gap:10,marginTop:8}}>
            <Btn color="#0891B2" icon={D.plus} loading={genLoading} onClick={generate}>
              Generate Slots
            </Btn>
            <Btn variant="secondary" onClick={()=>{
              setForm({doctorCode:"",slotDate:"",startTime:"",endTime:"",slotDuration:""});
              setGenRes(null);
            }}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — generation confirmation ── */}
          <ResponseCard
            title="Slots Generated Successfully"
            data={genRes}
            color="#0891B2"
            onClose={()=>setGenRes(null)}
          />
        </Card>
      )}

      {/* ─── VIEW ─── */}
      {tab==="view" && (
        <Card title="View Slots" icon={D.cal} color="#0891B2">
          <div style={{display:"flex",gap:10,alignItems:"flex-end",marginBottom:16,
            flexWrap:"wrap"}}>
            <Fld label="Doctor Code">
              <FInput value={doctorCode} onChange={e=>setDoctorCode(e.target.value)}
                placeholder="DOC-001" icon={D.search}/>
            </Fld>
            <Fld label="Date">
              <FInput value={date} onChange={e=>setDate(e.target.value)}
                type="date" icon={D.cal}/>
            </Fld>
            <Btn color="#0891B2" icon={D.search} loading={getLoading} onClick={getSlots}>
              Get Slots
            </Btn>
          </div>

          {slots.length===0
            ? <Empty emoji="🗓️" label="No slots loaded"
                hint="Enter doctor code and date, then click Get Slots"/>
            : <DT cols={[
                {k:"slotId",    l:"Slot ID"},
                {k:"startTime", l:"Start"},
                {k:"endTime",   l:"End"},
                {k:"status",    l:"Status",
                  r:r=><Badge status={r.status||"AVAILABLE"}/>},
              ]} rows={slots}/>
          }
        </Card>
      )}

      {/* ─── MANAGE ─── */}
      {tab==="manage" && (
        <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:20}}>
          <Card title="Book a Slot" icon={D.book} color="#059669">
            <Fld label="Slot ID">
              <FInput value={bookId} onChange={e=>setBookId(e.target.value)}
                placeholder="42" icon={D.search}/>
            </Fld>
            <Btn color="#059669" icon={D.book} loading={bookLoading} onClick={bookSlot}>
              Book Slot
            </Btn>

            {/* ── RESPONSE — booked slot confirmation ── */}
            <ResponseCard
              title="Slot Booked"
              data={bookRes}
              color="#059669"
              onClose={()=>setBookRes(null)}
            />
          </Card>

          <Card title="Release a Slot" icon={D.rel} color="#EF4444">
            <Fld label="Slot ID">
              <FInput value={releaseId} onChange={e=>setReleaseId(e.target.value)}
                placeholder="42" icon={D.search}/>
            </Fld>
            <Btn color="#EF4444" icon={D.rel} loading={releaseLoading}
              onClick={releaseSlot}>
              Release Slot
            </Btn>

            {/* ── RESPONSE — released slot confirmation ── */}
            <ResponseCard
              title="Slot Released"
              data={releaseRes}
              color="#EF4444"
              onClose={()=>setReleaseRes(null)}
            />
          </Card>
        </div>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}

/* ─── APPOINTMENT MODULE ──────────────────────────────────── */
const A0 = {patientId:"", doctorCode:"", slotId:"", appointmentDate:""};

function AppointmentModule() {
  const [tab, setTab]   = useState("create");
  const [form, setForm] = useState(A0);
  const [loading, setLoading]     = useState(false);
  const [toast, notify, setToast] = useToast();

  const [createRes, setCreateRes] = useState(null);

  const [cancelId, setCancelId]           = useState("");
  const [cancelLoading, setCancelLoading] = useState(false);
  const [cancelRes, setCancelRes]         = useState(null);

  const [resForm, setResForm] = useState({
    appointmentCode:"", newSlotId:"", newAppointmentDate:""
  });
  const [resLoading, setResLoading] = useState(false);
  const [reschedRes, setReschedRes] = useState(null);

  /* ── complete ── */
  const [completeId, setCompleteId]           = useState("");
  const [completeLoading, setCompleteLoading] = useState(false);
  const [completeRes, setCompleteRes]         = useState(null);

  const onChange    = key => e => setForm(prev=>({...prev,[key]:e.target.value}));
  const onResChange = key => e => setResForm(prev=>({...prev,[key]:e.target.value}));

  /* ─── CREATE ─── */
  const create = async () => {
    if (!form.patientId||!form.doctorCode||!form.slotId||!form.appointmentDate)
      return notify("Fill all fields","error");

    const payload = {
      patientId:       Number(form.patientId),
      doctorCode:      form.doctorCode,
      slotId:          Number(form.slotId),
      appointmentDate: form.appointmentDate,
    };

    if (Number.isNaN(payload.patientId))
      return notify("Patient ID must be a number","error");
    if (Number.isNaN(payload.slotId))
      return notify("Slot ID must be a number","error");

    setLoading(true);
    setCreateRes(null);
    try {
      const res = await API.post("/appointments", payload);
      setCreateRes(res.data);
      notify("Appointment created!");
      setForm(A0);
    } catch(e) {
      const msg = e?.response?.data?.message
               || Object.values(e?.response?.data||{}).join(", ")
               || e.message || "Appointment creation failed";
      notify(msg,"error");
    } finally { setLoading(false); }
  };

  /* ─── CANCEL ─── */
  const cancel = async () => {
    if (!cancelId.trim()) return notify("Enter appointment ID","error");
    const id = Number(cancelId);
    if (Number.isNaN(id)) return notify("Appointment ID must be a number","error");

    setCancelLoading(true);
    setCancelRes(null);
    try {
      const res = await API.patch(`/appointments/${id}/cancel`);
      setCancelRes(res.data);
      notify(`Appointment ${cancelId} cancelled`);
      setCancelId("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Cancellation failed";
      notify(msg,"error");
    } finally { setCancelLoading(false); }
  };

  /* ─── RESCHEDULE ─── */
  const resched = async () => {
    if (!resForm.appointmentCode||!resForm.newSlotId||!resForm.newAppointmentDate)
      return notify("Fill all fields","error");

    const payload = {
      appointmentCode:    resForm.appointmentCode,
      newSlotId:          Number(resForm.newSlotId),
      newAppointmentDate: resForm.newAppointmentDate,
    };

    if (Number.isNaN(payload.newSlotId))
      return notify("New Slot ID must be a number","error");

    setResLoading(true);
    setReschedRes(null);
    try {
      const res = await API.put("/appointments/reschedule", payload);
      setReschedRes(res.data);
      notify("Appointment rescheduled!");
      setResForm({appointmentCode:"",newSlotId:"",newAppointmentDate:""});
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Reschedule failed";
      notify(msg,"error");
    } finally { setResLoading(false); }
  };

  /* ─── COMPLETE ─── */
  const complete = async () => {
    if (!completeId.trim()) return notify("Enter appointment ID","error");
    const id = Number(completeId);
    if (Number.isNaN(id)) return notify("Appointment ID must be a number","error");

    setCompleteLoading(true);
    setCompleteRes(null);
    try {
      const res = await API.patch(`/appointments/${id}/complete`);
      setCompleteRes(res.data);
      notify(`Appointment ${completeId} marked as completed`);
      setCompleteId("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Complete failed";
      notify(msg,"error");
    } finally { setCompleteLoading(false); }
  };

  const TABS = [
    {id:"create",     label:"Create",     icon:D.plus},
    {id:"cancel",     label:"Cancel",     icon:D.cancel},
    {id:"reschedule", label:"Reschedule", icon:D.swap},
    {id:"complete",   label:"Complete",   icon:D.check},
  ];

  return (
    <div style={{padding:28}}>
      <PH
        icon="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
        title="Appointment Services"
        sub="Create, cancel, reschedule and complete appointments"
        bg="#ECFDF5" color="#059669"
      />
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#059669"/>

      {/* ─── CREATE ─── */}
      {tab==="create" && (
        <Card title="Create Appointment" icon={D.plus} color="#059669">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Patient ID" required>
              <FInput value={form.patientId} onChange={onChange("patientId")}
                placeholder="1" icon={D.search}/>
            </Fld>
            <Fld label="Doctor Code" required>
              <FInput value={form.doctorCode} onChange={onChange("doctorCode")}
                placeholder="DOC-001" icon={D.search}/>
            </Fld>
            <Fld label="Slot ID" required>
              <FInput value={form.slotId} onChange={onChange("slotId")}
                placeholder="42" icon={D.clock}/>
            </Fld>
            <Fld label="Appointment Date" required>
              <FInput value={form.appointmentDate} onChange={onChange("appointmentDate")}
                type="date" icon={D.cal}/>
            </Fld>
          </div>

          {(form.patientId||form.doctorCode) && (
            <div style={{background:"#F0FDF4",border:"1px solid #BBF7D0",
              borderRadius:10,padding:"14px 16px",marginBottom:16}}>
              <div style={{fontSize:11,fontWeight:700,color:"#16A34A",marginBottom:8}}>
                SUMMARY
              </div>
              <div style={{display:"grid",gridTemplateColumns:"repeat(4,1fr)",gap:8}}>
                {[["Patient",form.patientId||"—"],["Doctor",form.doctorCode||"—"],
                  ["Slot",form.slotId||"—"],["Date",form.appointmentDate||"—"]
                ].map(([l,v])=>(
                  <div key={l}>
                    <div style={{fontSize:11,color:"#64748B"}}>{l}</div>
                    <div style={{fontSize:13,fontWeight:600}}>{v}</div>
                  </div>
                ))}
              </div>
            </div>
          )}

          <div style={{display:"flex",gap:10}}>
            <Btn color="#059669" icon={D.plus} loading={loading} onClick={create}>
              Create Appointment
            </Btn>
            <Btn variant="secondary" onClick={()=>{setForm(A0);setCreateRes(null);}}>
              Reset
            </Btn>
          </div>

          <ResponseCard
            title="Appointment Created"
            data={createRes}
            color="#059669"
            onClose={()=>setCreateRes(null)}
          />
        </Card>
      )}

      {/* ─── CANCEL ─── */}
      {tab==="cancel" && (
        <Card title="Cancel Appointment" icon={D.cancel} color="#EF4444">
          <Fld label="Appointment ID">
            <FInput value={cancelId} onChange={e=>setCancelId(e.target.value)}
              placeholder="e.g. 42 (numeric ID)" icon={D.search}/>
          </Fld>
          <Btn color="#EF4444" icon={D.cancel} loading={cancelLoading} onClick={cancel}>
            Cancel Appointment
          </Btn>
          <ResponseCard
            title="Appointment Cancelled"
            data={cancelRes}
            color="#EF4444"
            onClose={()=>setCancelRes(null)}
          />
        </Card>
      )}

      {/* ─── RESCHEDULE ─── */}
      {tab==="reschedule" && (
        <Card title="Reschedule Appointment" icon={D.swap} color="#059669">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Appointment Code" required>
              <FInput value={resForm.appointmentCode}
                onChange={onResChange("appointmentCode")}
                placeholder="APPT-0042" icon={D.search}/>
            </Fld>
            <div/>
            <Fld label="New Slot ID" required>
              <FInput value={resForm.newSlotId}
                onChange={onResChange("newSlotId")}
                placeholder="99" icon={D.clock}/>
            </Fld>
            <Fld label="New Appointment Date" required>
              <FInput value={resForm.newAppointmentDate}
                onChange={onResChange("newAppointmentDate")}
                type="date" icon={D.cal}/>
            </Fld>
          </div>

          <div style={{display:"flex",gap:10}}>
            <Btn color="#059669" icon={D.swap} loading={resLoading} onClick={resched}>
              Confirm Reschedule
            </Btn>
            <Btn variant="secondary"
              onClick={()=>{
                setResForm({appointmentCode:"",newSlotId:"",newAppointmentDate:""});
                setReschedRes(null);
              }}>
              Reset
            </Btn>
          </div>

          <ResponseCard
            title="Appointment Rescheduled"
            data={reschedRes}
            color="#059669"
            onClose={()=>setReschedRes(null)}
          />
        </Card>
      )}

      {/* ─── COMPLETE ─── */}
      {tab==="complete" && (
        <Card title="Complete Appointment" icon={D.check} color="#0891B2">
          <Fld label="Appointment ID">
            <FInput value={completeId} onChange={e=>setCompleteId(e.target.value)}
              placeholder="e.g. 42 (numeric ID)" icon={D.search}
              onKeyDown={e=>e.key==="Enter"&&complete()}/>
          </Fld>
          <Btn color="#0891B2" icon={D.check} loading={completeLoading} onClick={complete}>
            Mark as Completed
          </Btn>
          <ResponseCard
            title="Appointment Completed"
            data={completeRes}
            color="#0891B2"
            onClose={()=>setCompleteRes(null)}
          />
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── BILLING MODULE ──────────────────────────────────────── */
const PM = ["CASH","CARD","UPI","INSURANCE","CHEQUE","NETBANKING"];

const BILL_I0 = {
  appointmentCode:"", patientId:"", doctorCode:"",
  totalAmount:"", paidAmount:"", paymentMode:""
};

const BILL_C0 = {
  appointmentCode:"", patientId:"", insuranceProvider:"",
  policyNumber:"", claimAmount:""
};

function BillingModule() {
  const [tab, setTab]   = useState("invoice");
  const [toast, notify, setToast] = useToast();

  /* ── invoice states ── */
  const [inv, setInv]         = useState(BILL_I0);
  const [invLoad, setInvLoad] = useState(false);
  const [invRes, setInvRes]   = useState(null);   // ← create invoice response

  const [retInvId, setRetInvId] = useState("");
  const [retInv, setRetInv]     = useState(null);
  const [retLoad, setRetLoad]   = useState(false);

  const [payId, setPayId]     = useState("");
  const [payLoad, setPayLoad] = useState(false);
  const [payRes, setPayRes]   = useState(null);   // ← pay confirmation

  /* ── claim states ── */
  const [claim, setClaim]         = useState(BILL_C0);
  const [claimLoad, setClaimLoad] = useState(false);
  const [claimRes, setClaimRes]   = useState(null); // ← submit claim response

  const [act, setAct]         = useState({claimNumber:"", action:"APPROVE", approvedAmount:""});
  const [actLoad, setActLoad] = useState(false);
  const [actRes, setActRes]   = useState(null);   // ← process claim response

  const [retClaimId, setRetClaimId]     = useState("");
  const [retClaim, setRetClaim]         = useState(null);
  const [retClaimLoad, setRetClaimLoad] = useState(false);

  const fi = key => e => setInv(p=>({...p,[key]:e.target.value}));
  const fc = key => e => setClaim(p=>({...p,[key]:e.target.value}));

  /* ─── CREATE INVOICE ─── */
  const createInv = async () => {
    if (!inv.appointmentCode||!inv.patientId||!inv.totalAmount||!inv.paymentMode)
      return notify("Fill required fields","error");

    setInvLoad(true);
    setInvRes(null);
    try {
      const payload = {
        ...inv,
        patientId:   Number(inv.patientId),    // Long
        totalAmount: Number(inv.totalAmount),  // Double
        paidAmount:  inv.paidAmount ? Number(inv.paidAmount) : 0,
      };
      const res = await API.post("/invoices", payload);
      setInvRes(res.data);                     // ← InvoiceResponseDto
      notify("Invoice created!");
      setInv(BILL_I0);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Invoice creation failed";
      notify(msg,"error");
    } finally { setInvLoad(false); }
  };

  /* ─── RETRIEVE INVOICE ─── */
  const retrieveInv = async () => {
    if (!retInvId.trim()) return notify("Enter invoice number","error");
    setRetLoad(true);
    setRetInv(null);
    try {
      const res = await API.get(`/invoices/${retInvId}`);
      setRetInv(res.data||null);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Invoice not found";
      notify(msg,"error");
    } finally { setRetLoad(false); }
  };

  /* ─── PAY INVOICE ─── */
  // pay returns void — build confirmation from invoiceNumber
  const payInv = async () => {
    if (!payId.trim()) return notify("Enter invoice number","error");
    setPayLoad(true);
    setPayRes(null);
    try {
      await API.put(`/invoices/${payId}/pay`);
      setPayRes({
        invoiceNumber: payId,
        status:        "PAID",
        result:        "Invoice marked as paid successfully",
      });
      notify(`Invoice ${payId} marked as paid`);
      setPayId("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Payment failed";
      notify(msg,"error");
    } finally { setPayLoad(false); }
  };

  /* ─── SUBMIT CLAIM ─── */
  const createClaim = async () => {
    if (!claim.appointmentCode||!claim.patientId||!claim.insuranceProvider||
        !claim.policyNumber||!claim.claimAmount)
      return notify("Fill all fields","error");

    setClaimLoad(true);
    setClaimRes(null);
    try {
      const payload = {
        ...claim,
        patientId:   Number(claim.patientId),   // Long
        claimAmount: Number(claim.claimAmount), // Double
      };
      const res = await API.post("/claims", payload);
      setClaimRes(res.data);                    // ← InsuranceClaimResponseDto
      notify("Claim submitted!");
      setClaim(BILL_C0);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Claim submission failed";
      notify(msg,"error");
    } finally { setClaimLoad(false); }
  };

  /* ─── PROCESS CLAIM (approve / reject / settle) ─── */
  // all three return void — build confirmation object
  const processClaim = async () => {
    if (!act.claimNumber.trim()) return notify("Enter claim number","error");
    if (act.action==="APPROVE"&&!act.approvedAmount)
      return notify("Enter approved amount","error");

    setActLoad(true);
    setActRes(null);
    try {
      if (act.action==="APPROVE") {
        await API.put(
          `/claims/${act.claimNumber}/approve`,
          null,
          {params:{approvedAmount: Number(act.approvedAmount)}}
        );
        setActRes({
          claimNumber:    act.claimNumber,
          action:         "APPROVED",
          approvedAmount: `₹${act.approvedAmount}`,
          result:         "Claim approved successfully",
        });
      } else if (act.action==="REJECT") {
        await API.put(`/claims/${act.claimNumber}/reject`);
        setActRes({
          claimNumber: act.claimNumber,
          action:      "REJECTED",
          result:      "Claim rejected successfully",
        });
      } else if (act.action==="SETTLE") {
        await API.put(`/claims/${act.claimNumber}/settle`);
        setActRes({
          claimNumber: act.claimNumber,
          action:      "SETTLED",
          result:      "Claim settled successfully",
        });
      }
      notify(`Claim ${act.claimNumber} → ${act.action}`);
      setAct({claimNumber:"", action:"APPROVE", approvedAmount:""});
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Action failed";
      notify(msg,"error");
    } finally { setActLoad(false); }
  };

  /* ─── RETRIEVE CLAIM ─── */
  const retrieveClaim = async () => {
    if (!retClaimId.trim()) return notify("Enter claim number","error");
    setRetClaimLoad(true);
    setRetClaim(null);
    try {
      const res = await API.get(`/claims/${retClaimId}`);
      setRetClaim(res.data||null);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Claim not found";
      notify(msg,"error");
    } finally { setRetClaimLoad(false); }
  };

  const TABS = [
    {id:"invoice", label:"Invoices", icon:D.money},
    {id:"claims",  label:"Claims",   icon:D.shield},
    {id:"status",  label:"Pay Invoice", icon:D.check},
  ];

  const acColor = {APPROVE:"#059669", REJECT:"#EF4444", SETTLE:"#3B82F6"}[act.action];

  return (
    <div style={{padding:28}}>
      <PH icon={D.money} title="Billing Services"
        sub="Invoices, claims and payment management"
        bg="#F0FDFA" color="#0F766E"/>
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#0F766E"/>

      {/* ─── INVOICE TAB ─── */}
      {tab==="invoice" && (
        <>
          <Card title="Create Invoice" icon={D.plus} color="#0F766E">
            <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
              <Fld label="Appointment Code" required>
                <FInput value={inv.appointmentCode} onChange={fi("appointmentCode")}
                  placeholder="APPT-001"/>
              </Fld>
              <Fld label="Patient ID" required>
                <FInput value={inv.patientId} onChange={fi("patientId")}
                  placeholder="1"/>
              </Fld>
              <Fld label="Doctor Code">
                <FInput value={inv.doctorCode} onChange={fi("doctorCode")}
                  placeholder="DOC-001"/>
              </Fld>
              <Fld label="Payment Mode" required>
                <FSel value={inv.paymentMode} onChange={fi("paymentMode")}>
                  <option value="">Select mode</option>
                  {PM.map(m=><option key={m}>{m}</option>)}
                </FSel>
              </Fld>
              <Fld label="Total Amount (₹)" required>
                <FInput value={inv.totalAmount} onChange={fi("totalAmount")}
                  type="number" placeholder="1000"/>
              </Fld>
              <Fld label="Paid Amount (₹)">
                <FInput value={inv.paidAmount} onChange={fi("paidAmount")}
                  type="number" placeholder="0"/>
              </Fld>
            </div>
            <div style={{display:"flex",gap:10,marginTop:8}}>
              <Btn color="#0F766E" icon={D.plus} loading={invLoad} onClick={createInv}>
                Create Invoice
              </Btn>
              <Btn variant="secondary" onClick={()=>{setInv(BILL_I0);setInvRes(null);}}>
                Reset
              </Btn>
            </div>
            {/* ── RESPONSE — created invoice details ── */}
            <ResponseCard
              title="Invoice Created"
              data={invRes}
              color="#0F766E"
              onClose={()=>setInvRes(null)}
            />
          </Card>

          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:20}}>
            <Card title="Retrieve Invoice" icon={D.search} color="#0F766E">
              <Fld label="Invoice Number">
                <FInput value={retInvId} onChange={e=>setRetInvId(e.target.value)}
                  onKeyDown={e=>e.key==="Enter"&&retrieveInv()}
                  placeholder="INV-001"/>
              </Fld>
              <Btn color="#0F766E" icon={D.search} loading={retLoad} onClick={retrieveInv}>
                Retrieve
              </Btn>
              {/* ── RESPONSE — full invoice details ── */}
              <ResponseCard
                title="Invoice Details"
                data={retInv}
                color="#0F766E"
                onClose={()=>setRetInv(null)}
              />
            </Card>

            <Card title="Pay Invoice" icon={D.check} color="#059669">
              <Fld label="Invoice Number">
                <FInput value={payId} onChange={e=>setPayId(e.target.value)}
                  onKeyDown={e=>e.key==="Enter"&&payInv()}
                  placeholder="INV-001"/>
              </Fld>
              <Btn color="#059669" icon={D.check} loading={payLoad} onClick={payInv}>
                Pay Invoice
              </Btn>
              {/* ── RESPONSE — payment confirmation ── */}
              <ResponseCard
                title="Payment Confirmed"
                data={payRes}
                color="#059669"
                onClose={()=>setPayRes(null)}
              />
            </Card>
          </div>
        </>
      )}

      {/* ─── CLAIMS TAB ─── */}
      {tab==="claims" && (
        <>
          <Card title="Submit Insurance Claim" icon={D.plus} color="#0F766E">
            <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
              <Fld label="Appointment Code" required>
                <FInput value={claim.appointmentCode} onChange={fc("appointmentCode")}
                  placeholder="APPT-001"/>
              </Fld>
              <Fld label="Patient ID" required>
                <FInput value={claim.patientId} onChange={fc("patientId")}
                  placeholder="1"/>
              </Fld>
              <Fld label="Insurance Provider" required>
                <FInput value={claim.insuranceProvider} onChange={fc("insuranceProvider")}
                  placeholder="Star Health"/>
              </Fld>
              <Fld label="Policy Number" required>
                <FInput value={claim.policyNumber} onChange={fc("policyNumber")}
                  placeholder="POL-12345"/>
              </Fld>
              <Fld label="Claim Amount (₹)" required>
                <FInput value={claim.claimAmount} onChange={fc("claimAmount")}
                  type="number" placeholder="5000"/>
              </Fld>
            </div>
            <div style={{display:"flex",gap:10,marginTop:8}}>
              <Btn color="#0F766E" icon={D.plus} loading={claimLoad} onClick={createClaim}>
                Submit Claim
              </Btn>
              <Btn variant="secondary" onClick={()=>{setClaim(BILL_C0);setClaimRes(null);}}>
                Reset
              </Btn>
            </div>
            {/* ── RESPONSE — submitted claim details ── */}
            <ResponseCard
              title="Claim Submitted"
              data={claimRes}
              color="#0F766E"
              onClose={()=>setClaimRes(null)}
            />
          </Card>

          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:20}}>
            <Card title="Approve / Reject / Settle" icon={D.shield} color="#7C3AED">
              <Fld label="Claim Number">
                <FInput value={act.claimNumber}
                  onChange={e=>setAct(p=>({...p,claimNumber:e.target.value}))}
                  placeholder="CLM-001"/>
              </Fld>
              <Fld label="Action">
                <FSel value={act.action}
                  onChange={e=>setAct(p=>({...p,action:e.target.value}))}>
                  <option value="APPROVE">Approve</option>
                  <option value="REJECT">Reject</option>
                  <option value="SETTLE">Settle</option>
                </FSel>
              </Fld>
              {act.action==="APPROVE" && (
                <Fld label="Approved Amount (₹)" required>
                  <FInput type="number"
                    value={act.approvedAmount}
                    onChange={e=>setAct(p=>({...p,approvedAmount:e.target.value}))}
                    placeholder="5000"/>
                </Fld>
              )}
              <Btn color={acColor} icon={D.check} loading={actLoad} onClick={processClaim}>
                Process
              </Btn>
              {/* ── RESPONSE — process confirmation ── */}
              <ResponseCard
                title="Claim Processed"
                data={actRes}
                color={acColor}
                onClose={()=>setActRes(null)}
              />
            </Card>

            <Card title="Retrieve Claim" icon={D.search} color="#0F766E">
              <Fld label="Claim Number">
                <FInput value={retClaimId}
                  onChange={e=>setRetClaimId(e.target.value)}
                  onKeyDown={e=>e.key==="Enter"&&retrieveClaim()}
                  placeholder="CLM-001"/>
              </Fld>
              <Btn color="#0F766E" icon={D.search} loading={retClaimLoad}
                onClick={retrieveClaim}>
                Retrieve
              </Btn>
              {/* ── RESPONSE — full claim details ── */}
              <ResponseCard
                title="Claim Details"
                data={retClaim}
                color="#0F766E"
                onClose={()=>setRetClaim(null)}
              />
            </Card>
          </div>
        </>
      )}

      {/* ─── STATUS / PAY TAB ─── */}
      {tab==="status" && (
        <Card title="Pay Invoice" icon={D.check} color="#0F766E">
          <Fld label="Invoice Number">
            <FInput value={payId} onChange={e=>setPayId(e.target.value)}
              placeholder="INV-001"
              onKeyDown={e=>e.key==="Enter"&&payInv()}/>
          </Fld>
          <Btn color="#059669" icon={D.check} loading={payLoad} onClick={payInv}>
            Mark as Paid
          </Btn>
          {/* ── RESPONSE — payment confirmation ── */}
          <ResponseCard
            title="Payment Confirmed"
            data={payRes}
            color="#059669"
            onClose={()=>setPayRes(null)}
          />
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── MINI BAR ───────────────────────────────────────────── */
function MiniBar({ value, max, color }) {
  const pct = max > 0 ? Math.round((value / max) * 100) : 0;
  return (
    <div style={{
      background: "#F1F5F9",
      borderRadius: 6,
      height: 6,
      overflow: "hidden"
    }}>
      <div style={{
        width: `${pct}%`,
        height: "100%",
        background: color,
        borderRadius: 6,
        transition: "width .4s ease"
      }} />
    </div>
  );
}
/* ─── ANALYTICS CALENDAR ──────────────────────────────────── */
function AnalyticsCalendar() {
  const today = new Date();
  const [year, setYear] = useState(today.getFullYear());
  const [month, setMonth] = useState(today.getMonth());
  const [selDate, setSelDate] = useState(null);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);
  const [toast, notify, setToast] = useToast();

  const MONTHS = [
    "January","February","March","April","May","June",
    "July","August","September","October","November","December"
  ];
  const DAYS = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  const prevMonth = () =>
    month === 0 ? (setMonth(11), setYear(y => y - 1)) : setMonth(m => m - 1);

  const nextMonth = () =>
    month === 11 ? (setMonth(0), setYear(y => y + 1)) : setMonth(m => m + 1);

  const selectDate = async day => {
    const d = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
    setSelDate(d);
    setLoading(true);
    try {
      const res = await API.get(`/analytics/daily?date=${d}`);
      setStats(res.data || {});
    } catch {
      notify("Failed to load stats", "error");
      setStats(null);
    } finally {
      setLoading(false);
    }
  };

  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`;

  const cells = [];
  for (let i = 0; i < firstDay; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);

  const maxVal = stats
    ? Math.max(
        stats.totalPatients || 0,
        stats.totalAppointments || 0,
        stats.totalAdmissions || 0,
        stats.activeAdmissions || 0,
        1
      )
    : 1;

  return(
    <div>
      <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:20}}>
        {/* Calendar */}
        <Card title="Select Date" icon={D.cal} color="#7C3AED">
          <div style={{display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:16}}>
            <button onClick={prevMonth} style={{background:"#F1F5F9",border:"none",borderRadius:8,padding:"6px 12px",cursor:"pointer",fontSize:16}}>‹</button>
            <span style={{fontFamily:"'Syne',sans-serif",fontWeight:700,color:"#1E293B"}}>{MONTHS[month]} {year}</span>
            <button onClick={nextMonth} style={{background:"#F1F5F9",border:"none",borderRadius:8,padding:"6px 12px",cursor:"pointer",fontSize:16}}>›</button>
          </div>
          <div style={{display:"grid",gridTemplateColumns:"repeat(7,1fr)",gap:4,marginBottom:8}}>
            {DAYS.map(d=><div key={d} style={{textAlign:"center",fontSize:11,fontWeight:700,color:"#94A3B8",padding:"4px 0"}}>{d}</div>)}
          </div>
          <div style={{display:"grid",gridTemplateColumns:"repeat(7,1fr)",gap:4}}>
            {cells.map((day,i)=>{
              if(!day) return <div key={`e${i}`}/>;
              const ds=`${year}-${String(month+1).padStart(2,"0")}-${String(day).padStart(2,"0")}`;
              const isToday=ds===todayStr;
              const isSel=ds===selDate;
              return(
                <div key={day} onClick={()=>selectDate(day)}
                  style={{textAlign:"center",padding:"8px 4px",borderRadius:9,cursor:"pointer",fontSize:13,fontWeight:isSel||isToday?700:400,background:isSel?"#7C3AED":isToday?"#F5F3FF":"transparent",color:isSel?"#fff":isToday?"#7C3AED":"#334155",border:`1.5px solid ${isSel?"#7C3AED":isToday?"#DDD6FE":"transparent"}`,transition:"all .15s"}}>
                  {day}
                </div>
              );
            })}
          </div>
          {selDate&&<div style={{marginTop:16,textAlign:"center",fontSize:12,color:"#7C3AED",fontWeight:600}}>📅 {selDate}</div>}
        </Card>

        {/* Stats panel */}
        <Card title={selDate?`Stats for ${selDate}`:"Daily Statistics"} icon={D.chart} color="#7C3AED">
          {!selDate&&!loading&&<Empty emoji="📅" label="Select a date" hint="Click any date on the calendar to view statistics"/>}
          {loading&&<div style={{textAlign:"center",padding:"40px",color:"#94A3B8"}}>Loading stats…</div>}
          {stats&&!loading&&(
            <div style={{animation:"fadeIn .3s ease"}}>
              {[
                {label:"Total Patients",     key:"totalPatients",      color:"#3B82F6",  icon:"👤"},
                {label:"Total Appointments", key:"totalAppointments",  color:"#059669",  icon:"📋"},
                {label:"Total Admissions",   key:"totalAdmissions",    color:"#D97706",  icon:"🏥"},
                {label:"Active Admissions",  key:"activeAdmissions",   color:"#DC2626",  icon:"🛏️"},
              ].map(s=>(
                <div key={s.key} style={{marginBottom:16}}>
                  <div style={{display:"flex",alignItems:"center",justifyContent:"space-between",marginBottom:4}}>
                    <span style={{fontSize:13,color:"#64748B",fontWeight:500}}>{s.icon} {s.label}</span>
                    <span style={{fontFamily:"'Syne',sans-serif",fontSize:20,fontWeight:700,color:s.color}}>{stats[s.key]??0}</span>
                  </div>
                  <MiniBar value={stats[s.key]||0} max={maxVal} color={s.color}/>
                </div>
              ))}
              <div style={{borderTop:"1px solid #F1F5F9",paddingTop:16,marginTop:4}}>
                <div style={{display:"flex",alignItems:"center",justifyContent:"space-between"}}>
                  <span style={{fontSize:13,color:"#64748B",fontWeight:500}}>💰 Total Revenue</span>
                  <span style={{fontFamily:"'Syne',sans-serif",fontSize:20,fontWeight:700,color:"#0F766E"}}>₹{stats.totalRevenue?.toLocaleString()??0}</span>
                </div>
                <div style={{background:"#F0FDFA",borderRadius:8,padding:"8px 12px",marginTop:10,display:"flex",justifyContent:"space-between"}}>
                  <span style={{fontSize:12,color:"#64748B"}}>Avg per patient</span>
                  <span style={{fontSize:12,fontWeight:700,color:"#0F766E"}}>₹{stats.totalPatients?Math.round((stats.totalRevenue||0)/stats.totalPatients).toLocaleString():0}</span>
                </div>
              </div>
            </div>
          )}
        </Card>
      </div>
      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}


/* ─── ANALYTICS MODULE ────────────────────────────────────── */
const REPORT_TYPES = [
  "PATIENT_SUMMARY",
  "DOCTOR_PERFORMANCE",
  "BILLING_SUMMARY",
  "WARD_UTILIZATION",
  "APPOINTMENT_ANALYTICS",
];

function AnalyticsModule() {
  const [tab, setTab] = useState("calendar");
  const [toast, notify, setToast] = useToast();

  /* generate */
  const [genForm, setGenForm] = useState({reportName:"", reportType:""});
  const [genLoad, setGenLoad] = useState(false);
  const [genRes, setGenRes]   = useState(null);   // ← ReportResponseDto

  /* download */
  const [dlId, setDlId]     = useState("");
  const [dlLoad, setDlLoad] = useState(false);

  /* all reports */
  const [allReports, setAllReports] = useState([]);
  const [allLoad, setAllLoad]       = useState(false);

  /* ─── GENERATE REPORT ─── */
  const genRep = async () => {
    if (!genForm.reportName||!genForm.reportType)
      return notify("Fill all fields","error");

    setGenLoad(true);
    setGenRes(null);
    try {
      // ReportController is at /analytics/reports (no /api prefix)
      const res = await API.post("/analytics/reports", genForm);
      setGenRes(res.data);                    // ← ReportResponseDto
      notify("Report generated!");
      setGenForm({reportName:"", reportType:""});
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Generation failed";
      notify(msg,"error");
    } finally { setGenLoad(false); }
  };

  /* ─── DOWNLOAD REPORT ─── */
  const dlRep = async (id) => {
    const reportId = id || dlId;
    if (!String(reportId).trim()) return notify("Enter report ID","error");

    setDlLoad(true);
    try {
      const res = await API.get(
        `/analytics/reports/${reportId}/download`,
        {responseType:"blob"}
      );
      const url = URL.createObjectURL(res.data);
      const a   = document.createElement("a");
      a.href     = url;
      a.download = `Hospital_Report_${reportId}.pdf`;
      a.click();
      URL.revokeObjectURL(url);
      notify(`Report ${reportId} downloaded`);
      if (!id) setDlId("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Download failed";
      notify(msg,"error");
    } finally { setDlLoad(false); }
  };

  /* ─── GET ALL REPORTS ─── */
  const getAll = async () => {
    setAllLoad(true);
    try {
      const res = await API.get("/analytics/reports");
      setAllReports(Array.isArray(res.data)?res.data:[]);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Failed to load reports";
      notify(msg,"error");
      setAllReports([]);
    } finally { setAllLoad(false); }
  };

  const TABS = [
    {id:"calendar", label:"Calendar View",   icon:D.cal},
    {id:"generate", label:"Generate Report", icon:D.plus},
    {id:"download", label:"Download",         icon:D.dl},
    {id:"all",      label:"All Reports",      icon:D.list},
  ];

  return (
    <div style={{padding:28}}>
      <PH icon={D.chart} title="Analytics & KPI"
        sub="Calendar insights, generate and download reports"
        bg="#F5F3FF" color="#7C3AED"/>
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#7C3AED"/>

      {/* ─── CALENDAR ─── */}
      {tab==="calendar" && <AnalyticsCalendar/>}

      {/* ─── GENERATE ─── */}
      {tab==="generate" && (
        <Card title="Generate Report" icon={D.plus} color="#7C3AED">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Report Name" required>
              <FInput value={genForm.reportName}
                onChange={e=>setGenForm(p=>({...p,reportName:e.target.value}))}
                placeholder="Q1 Patient Summary 2025"/>
            </Fld>
            <Fld label="Report Type" required>
              <FSel value={genForm.reportType}
                onChange={e=>setGenForm(p=>({...p,reportType:e.target.value}))}>
                <option value="">Select type</option>
                {REPORT_TYPES.map(t=>(
                  <option key={t} value={t}>{t.replace(/_/g," ")}</option>
                ))}
              </FSel>
            </Fld>
          </div>

          <div style={{display:"flex",gap:10,marginTop:8}}>
            <Btn color="#7C3AED" icon={D.chart} loading={genLoad} onClick={genRep}>
              Generate Report
            </Btn>
            <Btn variant="secondary" onClick={()=>{
              setGenForm({reportName:"",reportType:""});
              setGenRes(null);
            }}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — generated report details ── */}
          <ResponseCard
            title="Report Generated Successfully"
            data={genRes}
            color="#7C3AED"
            onClose={()=>setGenRes(null)}
          />

          {/* quick download button if report was just generated */}
          {genRes?.reportId && (
            <div style={{marginTop:12}}>
              <Btn color="#7C3AED" icon={D.dl} loading={dlLoad}
                onClick={()=>dlRep(genRes.reportId)}>
                Download This Report
              </Btn>
            </div>
          )}
        </Card>
      )}

      {/* ─── DOWNLOAD ─── */}
      {tab==="download" && (
        <Card title="Download Report by ID" icon={D.dl} color="#7C3AED">
          <Fld label="Report ID">
            <FInput value={dlId} onChange={e=>setDlId(e.target.value)}
              placeholder="RPT-0001" icon={D.search}
              onKeyDown={e=>e.key==="Enter"&&dlRep()}/>
          </Fld>
          <Btn color="#7C3AED" icon={D.dl} loading={dlLoad} onClick={()=>dlRep()}>
            Download
          </Btn>
        </Card>
      )}

      {/* ─── ALL REPORTS ─── */}
      {tab==="all" && (
        <>
          <div style={{marginBottom:16}}>
            <Btn color="#7C3AED" icon={D.refresh} loading={allLoad} onClick={getAll}
              style={{padding:"7px 14px",fontSize:12}}>
              {allReports.length?"Refresh":"Load All"}
            </Btn>
          </div>

          {allReports.length===0 && !allLoad && (
            <Empty emoji="📊" label="No reports loaded" hint='Click "Load All"'/>
          )}

          {allReports.length>0 && (
            <DT rows={allReports} cols={[
              {k:"reportId",   l:"ID"},
              {k:"reportName", l:"Name"},
              {k:"generatedBy",l:"Generated By"},
              {k:"createdAt",  l:"Created At",
                r:r=>r.createdAt
                  ? new Date(r.createdAt).toLocaleString("en-IN",
                      {day:"numeric",month:"short",year:"numeric",
                       hour:"2-digit",minute:"2-digit"})
                  : "—"
              },
              {k:"status", l:"Status",
                r:r=><Badge status={r.status||"COMPLETED"}/>
              },
              {k:"_dl", l:"Download",
                r:r=>(
                  <button
                    onClick={()=>dlRep(r.reportId)}
                    style={{background:"#F5F3FF",color:"#7C3AED",
                      border:"1px solid #DDD6FE",borderRadius:6,
                      padding:"4px 10px",fontSize:12,fontWeight:600,
                      cursor:"pointer"}}>
                    ⬇ Download
                  </button>
                )
              },
            ]}/>
          )}
        </>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── WARD MODULE ─────────────────────────────────────────── */
const WC = {
  GENERAL:      {c:"#2563EB", bg:"#DBEAFE", l:"General"},
  SEMI_PRIVATE: {c:"#7C3AED", bg:"#EDE9FE", l:"Semi-Private"},
  PRIVATE:      {c:"#059669", bg:"#D1FAE5", l:"Private"},
  ICU:          {c:"#DC2626", bg:"#FEE2E2", l:"ICU"},
};

function BedGrid({beds, wardType}) {
  const [hov,sh] = useState(null);
  if (!beds||!beds.length)
    return <Empty emoji="🛏️" label="No beds loaded" hint="Select a ward type and click Load Beds"/>;

  const wc    = WC[wardType]||WC.GENERAL;
  const avail = beds.filter(b=>b.status==="AVAILABLE"||b.status==="FREE").length;
  const occ   = beds.filter(b=>b.status==="OCCUPIED").length;

  return (
    <div>
      <div style={{display:"flex",gap:12,marginBottom:16,flexWrap:"wrap"}}>
        {[{c:"#16A34A",bg:"#DCFCE7",l:"Available"},{c:"#DC2626",bg:"#FEE2E2",l:"Occupied"}].map(x=>(
          <div key={x.l} style={{display:"flex",alignItems:"center",gap:6}}>
            <div style={{width:18,height:18,borderRadius:4,background:x.bg,border:`2px solid ${x.c}`}}/>
            <span style={{fontSize:12,fontWeight:600,color:"#64748B"}}>{x.l}</span>
          </div>
        ))}
        <span style={{marginLeft:"auto",fontSize:12,fontWeight:600,color:wc.c}}>
          {avail} Available · {occ} Occupied
        </span>
      </div>
      <div style={{background:`linear-gradient(135deg,${wc.c},${wc.c}cc)`,
        borderRadius:"12px 12px 0 0",padding:"10px 16px"}}>
        <span style={{fontFamily:"'Syne',sans-serif",fontSize:13,fontWeight:700,color:"#fff"}}>
          {wc.l} Ward — {beds.length} Beds
        </span>
      </div>
      <div style={{background:wc.bg,borderLeft:`2px solid ${wc.c}`,
        borderRight:`2px solid ${wc.c}`,padding:"6px",textAlign:"center"}}>
        <span style={{fontSize:11,fontWeight:700,color:wc.c,letterSpacing:"0.06em"}}>
          🏥 NURSE STATION
        </span>
      </div>
      <div style={{background:"#F8FAFC",border:`1.5px solid ${wc.c}40`,
        borderTop:"none",borderRadius:"0 0 12px 12px",padding:20}}>
        <div style={{display:"flex",flexWrap:"wrap",gap:10,justifyContent:"center"}}>
          {beds.map(bed=>{
            const av  = bed.status==="AVAILABLE"||bed.status==="FREE";
            const bc  = av?"#16A34A":"#DC2626";
            const bbg = av?"#DCFCE7":"#FEE2E2";
            return (
              <div key={bed.bedNumber}
                onMouseEnter={()=>sh(bed.bedNumber)} onMouseLeave={()=>sh(null)}
                style={{position:"relative",width:58,height:50,borderRadius:9,background:bbg,
                  border:`2px solid ${hov===bed.bedNumber?bc:bc+"70"}`,
                  display:"flex",flexDirection:"column",alignItems:"center",
                  justifyContent:"center",cursor:"pointer",transition:"all .15s",
                  transform:hov===bed.bedNumber?"scale(1.08)":"scale(1)",
                  boxShadow:hov===bed.bedNumber?`0 4px 14px ${bc}40`:"none"}}>
                <svg width="20" height="16" viewBox="0 0 24 18" fill="none">
                  <rect x="1" y="6" width="22" height="10" rx="2" fill={bc} opacity=".2"/>
                  <rect x="1" y="6" width="22" height="10" rx="2" stroke={bc} strokeWidth="1.5"/>
                  <rect x="2" y="3" width="6" height="5" rx="1.5" fill={bc} opacity=".4"/>
                  <line x1="1" y1="16" x2="1" y2="18" stroke={bc} strokeWidth="1.5"/>
                  <line x1="23" y1="16" x2="23" y2="18" stroke={bc} strokeWidth="1.5"/>
                </svg>
                <span style={{fontSize:10,fontWeight:700,color:bc,marginTop:2}}>
                  {bed.bedNumber}
                </span>
                {hov===bed.bedNumber && (
                  <div style={{position:"absolute",bottom:"calc(100% + 6px)",left:"50%",
                    transform:"translateX(-50%)",background:"#1E293B",color:"#fff",
                    fontSize:11,fontWeight:600,padding:"5px 9px",borderRadius:7,
                    whiteSpace:"nowrap",zIndex:100,boxShadow:"0 4px 12px rgba(0,0,0,.25)"}}>
                    Bed {bed.bedNumber} · {bed.status||"Unknown"}
                    {bed.patientCode?` · ${bed.patientCode}`:""}
                    <div style={{position:"absolute",bottom:-4,left:"50%",
                      transform:"translateX(-50%)",width:8,height:8,
                      background:"#1E293B",clipPath:"polygon(0 0,100% 0,50% 100%)"}}/>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
      <div style={{display:"flex",gap:10,marginTop:14}}>
        {[
          {l:"Total",    v:beds.length, c:wc.c},
          {l:"Available",v:avail,       c:"#16A34A"},
          {l:"Occupied", v:occ,         c:"#DC2626"},
          {l:"Occupancy",v:`${Math.round(occ/beds.length*100)||0}%`, c:"#D97706"},
        ].map(s=>(
          <div key={s.l} style={{flex:1,background:"#fff",borderRadius:10,
            padding:"12px 14px",border:"1px solid #E2E8F0",textAlign:"center"}}>
            <div style={{fontFamily:"'Syne',sans-serif",fontSize:22,
              fontWeight:800,color:s.c}}>{s.v}</div>
            <div style={{fontSize:11,color:"#64748B",fontWeight:600,marginTop:2}}>{s.l}</div>
          </div>
        ))}
      </div>
    </div>
  );
}

function WardModule() {
  const [tab, st]   = useState("blueprint");
  const [toast, notify, setToast] = useToast();

  /* blueprint */
  const [selW, setSelW]         = useState("GENERAL");
  const [beds, setBeds]         = useState([]);
  const [bedsLoad, setBedsLoad] = useState(false);

  /* admit */
  const [admitForm, setAdmitForm] = useState({
    patientCode:"", doctorCode:"", wardType:"", bedNumber:""
  });
  const [admitLoad, setAdmitLoad] = useState(false);
  const [admitRes, setAdmitRes]   = useState(null);  // ← WardAdmissionResponseDto

  /* details */
  const [admCode, setAdmCode] = useState("");
  const [admData, setAdmData] = useState(null);      // ← WardAdmissionResponseDto
  const [admLoad, setAdmLoad] = useState(false);

  /* active */
  const [activeAdmissions, setActiveAdmissions] = useState(0);
  const [activeLoad, setActiveLoad]             = useState(false);

  /* discharge */
  const [discCode, setDiscCode] = useState("");
  const [discLoad, setDiscLoad] = useState(false);
  const [discRes, setDiscRes]   = useState(null);    // ← discharge confirmation

  const wf = key => e => setAdmitForm(prev=>({...prev,[key]:e.target.value}));

  /* ── LOAD BEDS ── */
  const loadBeds = async () => {
    setBedsLoad(true);
    try {
      const res = await API.get(`/wards/beds?wardType=${selW}`);
      setBeds(Array.isArray(res.data)?res.data:[]);
    } catch { notify("Failed to load beds","error"); setBeds([]); }
    finally { setBedsLoad(false); }
  };

  /* ── ADMIT ── */
  const admit = async () => {
    if (!admitForm.patientCode||!admitForm.doctorCode||
        !admitForm.wardType||!admitForm.bedNumber)
      return notify("Fill all fields","error");

    setAdmitLoad(true);
    setAdmitRes(null);
    try {
      const res = await API.post("/wards/admit", admitForm);
      setAdmitRes(res.data);                 // ← WardAdmissionResponseDto
      notify("Patient admitted!");
      setAdmitForm({patientCode:"",doctorCode:"",wardType:"",bedNumber:""});
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Admission failed";
      notify(msg,"error");
    } finally { setAdmitLoad(false); }
  };

  /* ── GET ADMISSION DETAILS ── */
  const getAdmission = async () => {
    if (!admCode.trim()) return notify("Enter admission code","error");
    setAdmLoad(true);
    setAdmData(null);
    try {
      const res = await API.get(`/wards/${admCode}`);
      setAdmData(res.data||null);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Admission not found";
      notify(msg,"error");
    } finally { setAdmLoad(false); }
  };

  /* ── ACTIVE ADMISSIONS ── */
  const getActive = async () => {
    setActiveLoad(true);
    try {
      const res = await API.get("/wards/admissions/active");
      setActiveAdmissions(Number(res.data)||0);
    } catch { notify("Failed to load active admissions","error"); setActiveAdmissions(0); }
    finally { setActiveLoad(false); }
  };

  /* ── DISCHARGE ── */
  // discharge returns void — build confirmation object
  const discharge = async () => {
    if (!discCode.trim()) return notify("Enter admission code","error");
    setDiscLoad(true);
    setDiscRes(null);
    try {
      await API.put(`/wards/${discCode}/discharge`);
      setDiscRes({
        admissionCode: discCode,
        status:        "DISCHARGED",
        result:        "Patient discharged successfully",
      });
      notify(`Patient discharged — ${discCode}`);
      setDiscCode("");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Discharge failed";
      notify(msg,"error");
    } finally { setDiscLoad(false); }
  };

  const TABS = [
    {id:"blueprint", label:"Blueprint",         icon:D.bed},
    {id:"admit",     label:"Admit Patient",     icon:D.admit},
    {id:"details",   label:"Admission Details", icon:D.search},
    {id:"active",    label:"Active Admissions", icon:D.list},
    {id:"discharge", label:"Discharge",          icon:D.disc},
  ];

  return (
    <div style={{padding:28}}>
      <PH icon={D.ward} title="Ward Services"
        sub="Bed blueprint, admissions and discharge"
        bg="#EFF6FF" color="#1D4ED8"/>
      <Tabs tabs={TABS} active={tab} setActive={st} color="#1D4ED8"/>

      {/* ── BLUEPRINT ── */}
      {tab==="blueprint" && (
        <Card title="Live Ward Blueprint" icon={D.bed} color="#1D4ED8"
          action={
            <Btn color="#1D4ED8" icon={D.refresh} loading={bedsLoad} onClick={loadBeds}
              style={{padding:"7px 14px",fontSize:12}}>
              Load Beds
            </Btn>
          }>
          <div style={{display:"flex",gap:10,marginBottom:20,flexWrap:"wrap"}}>
            {Object.entries(WC).map(([k,v])=>(
              <button key={k} onClick={()=>{setSelW(k);setBeds([]);}}
                style={{display:"flex",alignItems:"center",gap:8,padding:"9px 16px",
                  borderRadius:10,border:`2px solid ${selW===k?v.c:v.c+"40"}`,
                  background:selW===k?v.bg:"#fff",color:selW===k?v.c:"#64748B",
                  fontSize:13,fontWeight:700,cursor:"pointer"}}>
                <div style={{width:10,height:10,borderRadius:"50%",background:v.c}}/>
                {v.l}
              </button>
            ))}
          </div>
          <BedGrid beds={beds} wardType={selW}/>
        </Card>
      )}

      {/* ── ADMIT ── */}
      {tab==="admit" && (
        <Card title="Admit Patient" icon={D.admit} color="#1D4ED8">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Patient Code" required>
              <FInput value={admitForm.patientCode} onChange={wf("patientCode")}
                placeholder="PAT-001"/>
            </Fld>
            <Fld label="Doctor Code" required>
              <FInput value={admitForm.doctorCode} onChange={wf("doctorCode")}
                placeholder="DOC-001"/>
            </Fld>
            <Fld label="Ward Type" required>
              <FSel value={admitForm.wardType} onChange={wf("wardType")}>
                <option value="">Select ward</option>
                {Object.entries(WC).map(([k,v])=>(
                  <option key={k} value={k}>{v.l}</option>
                ))}
              </FSel>
            </Fld>
            <Fld label="Bed Number" required>
              <FInput value={admitForm.bedNumber} onChange={wf("bedNumber")}
                placeholder="B1"/>
            </Fld>
          </div>
          <div style={{display:"flex",gap:10,marginTop:8}}>
            <Btn color="#1D4ED8" icon={D.admit} loading={admitLoad} onClick={admit}>
              Admit Patient
            </Btn>
            <Btn variant="secondary" onClick={()=>{
              setAdmitForm({patientCode:"",doctorCode:"",wardType:"",bedNumber:""});
              setAdmitRes(null);
            }}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — admission details like Swagger ── */}
          <ResponseCard
            title="Patient Admitted Successfully"
            data={admitRes}
            color="#1D4ED8"
            onClose={()=>setAdmitRes(null)}
          />
        </Card>
      )}

      {/* ── DETAILS ── */}
      {tab==="details" && (
        <Card title="Get Admission by Code" icon={D.search} color="#1D4ED8">
          <Fld label="Admission Code">
            <FInput value={admCode} onChange={e=>setAdmCode(e.target.value)}
              onKeyDown={e=>e.key==="Enter"&&getAdmission()}
              placeholder="ADM-001"/>
          </Fld>
          <Btn color="#1D4ED8" icon={D.search} loading={admLoad} onClick={getAdmission}>
            Get Details
          </Btn>

          {/* ── RESPONSE — full admission record ── */}
          <ResponseCard
            title="Admission Details"
            data={admData}
            color="#1D4ED8"
            onClose={()=>setAdmData(null)}
          />
        </Card>
      )}

      {/* ── ACTIVE ── */}
      {tab==="active" && (
        <Card title="Active Admissions" icon={D.list} color="#059669"
          action={
            <Btn color="#059669" icon={D.refresh} loading={activeLoad} onClick={getActive}
              style={{padding:"7px 14px",fontSize:12}}>
              Refresh
            </Btn>
          }>
          {activeLoad && (
            <div style={{textAlign:"center",padding:32,color:"#94A3B8"}}>Loading…</div>
          )}
          {!activeLoad && activeAdmissions===0 && (
            <Empty emoji="🏥" label="No active admissions" hint="Click Refresh to load"/>
          )}
          {!activeLoad && activeAdmissions>0 && (
            <div style={{textAlign:"center",padding:"24px 0"}}>
              <div style={{fontFamily:"'Syne',sans-serif",fontSize:64,fontWeight:800,
                color:"#059669",lineHeight:1}}>{activeAdmissions}</div>
              <div style={{fontSize:14,color:"#64748B",marginTop:8,fontWeight:500}}>
                Patients currently admitted
              </div>
            </div>
          )}
        </Card>
      )}

      {/* ── DISCHARGE ── */}
      {tab==="discharge" && (
        <Card title="Discharge Patient" icon={D.disc} color="#EF4444">
          <Fld label="Admission Code">
            <FInput value={discCode} onChange={e=>setDiscCode(e.target.value)}
              onKeyDown={e=>e.key==="Enter"&&discharge()}
              placeholder="ADM-001"/>
          </Fld>
          <Btn color="#EF4444" icon={D.disc} loading={discLoad} onClick={discharge}>
            Discharge
          </Btn>

          {/* ── RESPONSE — discharge confirmation ── */}
          <ResponseCard
            title="Patient Discharged"
            data={discRes}
            color="#EF4444"
            onClose={()=>setDiscRes(null)}
          />
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── NOTIFICATION MODULE ─────────────────────────────────── */
const NT = ["EMAIL","SMS","PUSH","IN_APP"];
const RT = [
  "PATIENT","DOCTOR","NURSE","RECEPTIONIST",
  "ADMIN","BILLING_OFFICER","COMPLIANCE_OFFICER"
];

const N0 = {
  recipientId:"", recipientType:"", message:"",
  notificationType:"", recipientEmail:"", recipientPhone:"",
};

function NotificationModule() {
  const [tab, setTab]   = useState("create");
  const [form, setForm] = useState(N0);
  const [loading, setLoading]     = useState(false);
  const [toast, notify, setToast] = useToast();

  const [createRes, setCreateRes] = useState(null); // ← NotificationResponseDto

  const [retId, setRetId]       = useState("");
  const [retType, setRetType]   = useState("");
  const [retData, setRetData]   = useState([]);
  const [retLoading, setRetLoading] = useState(false);

  const onChange = key => e => setForm(prev=>({...prev,[key]:e.target.value}));

  /* ─── SEND NOTIFICATION ─── */
  const create = async () => {
    if (!form.recipientId||!form.recipientType||!form.message||!form.notificationType)
      return notify("Fill required fields","error");

    // recipientId is Long in backend
    const recipientId = Number(form.recipientId);
    if (Number.isNaN(recipientId))
      return notify("Recipient ID must be a number","error");

    setLoading(true);
    setCreateRes(null);
    try {
      const payload = {
        ...form,
        recipientId,                        // ← Long
        recipientEmail: form.recipientEmail||null,
        recipientPhone: form.recipientPhone||null,
        message:        form.message||null,
      };
      const res = await API.post("/notifications", payload);
      setCreateRes(res.data);               // ← NotificationResponseDto
      notify("Notification sent!");
      setForm(N0);
    } catch(e) {
      const msg = e?.response?.data?.message
               || Object.values(e?.response?.data||{}).join(", ")
               || e.message || "Notification failed";
      notify(msg,"error");
    } finally { setLoading(false); }
  };

  /* ─── RETRIEVE NOTIFICATIONS ─── */
  const retrieve = async () => {
    if (!retId.trim()||!retType)
      return notify("Enter recipient ID and type","error");

    // recipientId is Long in backend query param
    const recipientId = Number(retId);
    if (Number.isNaN(recipientId))
      return notify("Recipient ID must be a number","error");

    setRetLoading(true);
    setRetData([]);
    try {
      const res = await API.get(
        `/notifications?recipientId=${recipientId}&recipientType=${retType}`
      );
      setRetData(Array.isArray(res.data)?res.data:[]);
      if ((res.data||[]).length===0) notify("No notifications found","error");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Failed to retrieve notifications";
      notify(msg,"error");
    } finally { setRetLoading(false); }
  };

  const TABS = [
    {id:"create",   label:"Send Notification", icon:D.plus},
    {id:"retrieve", label:"Retrieve",           icon:D.search},
  ];

  return (
    <div style={{padding:28}}>
      <PH icon={D.notif} title="Notification Service"
        sub="Send and retrieve system notifications"
        bg="#FEF2F2" color="#DC2626"/>
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#DC2626"/>

      {/* ─── CREATE ─── */}
      {tab==="create" && (
        <Card title="Send Notification" icon={D.plus} color="#DC2626">
          <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
            <Fld label="Recipient ID" required>
              <FInput value={form.recipientId} onChange={onChange("recipientId")}
                placeholder="e.g. 1 (numeric ID)" icon={D.search}/>
            </Fld>
            <Fld label="Recipient Type" required>
              <FSel value={form.recipientType} onChange={onChange("recipientType")}>
                <option value="">Select type</option>
                {RT.map(t=><option key={t} value={t}>{t}</option>)}
              </FSel>
            </Fld>
            <Fld label="Notification Type" required>
              <FSel value={form.notificationType} onChange={onChange("notificationType")}>
                <option value="">Select channel</option>
                {NT.map(t=><option key={t} value={t}>{t}</option>)}
              </FSel>
            </Fld>
            <Fld label="Recipient Email">
              <FInput value={form.recipientEmail} onChange={onChange("recipientEmail")}
                placeholder="recipient@email.com" type="email" icon={D.mail}/>
            </Fld>
            <Fld label="Recipient Phone">
              <FInput value={form.recipientPhone} onChange={onChange("recipientPhone")}
                placeholder="9876543210" type="tel"/>
            </Fld>
            <div style={{gridColumn:"1/-1"}}>
              <Fld label="Message" required>
                <textarea value={form.message} onChange={onChange("message")} rows={4}
                  placeholder="Enter notification message…"
                  style={{...IS, resize:"vertical", paddingTop:10}}/>
              </Fld>
            </div>
          </div>

          <div style={{display:"flex",gap:10}}>
            <Btn color="#DC2626" icon={D.notif} loading={loading} onClick={create}>
              Send Notification
            </Btn>
            <Btn variant="secondary" onClick={()=>{setForm(N0);setCreateRes(null);}}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — sent notification details ── */}
          <ResponseCard
            title="Notification Sent Successfully"
            data={createRes}
            color="#DC2626"
            onClose={()=>setCreateRes(null)}
          />
        </Card>
      )}

      {/* ─── RETRIEVE ─── */}
      {tab==="retrieve" && (
        <Card title="Retrieve Notifications" icon={D.search} color="#DC2626">
          <div style={{display:"flex",gap:10,alignItems:"flex-end",flexWrap:"wrap",
            marginBottom:16}}>
            <Fld label="Recipient ID">
              <FInput value={retId} onChange={e=>setRetId(e.target.value)}
                placeholder="e.g. 1 (numeric ID)" icon={D.search}/>
            </Fld>
            <Fld label="Recipient Type">
              <FSel value={retType} onChange={e=>setRetType(e.target.value)}>
                <option value="">Select type</option>
                {RT.map(t=><option key={t} value={t}>{t}</option>)}
              </FSel>
            </Fld>
            <Btn color="#DC2626" icon={D.search} loading={retLoading} onClick={retrieve}>
              Retrieve
            </Btn>
          </div>

          {retData.length===0 && (
            <Empty emoji="🔔" label="No notifications loaded"
              hint="Enter recipient ID and type, then click Retrieve"/>
          )}

          {retData.length>0 && (
            <DT rows={retData} cols={[
              /* ✅ field is "id" not "notificationId" per NotificationResponseDto */
              {k:"id",      l:"ID"},
              {k:"message", l:"Message"},
              {k:"status",  l:"Status",
                r:r=><Badge status={r.status||"SENT"}/>
              },
              {k:"createdAt", l:"Sent At",
                r:r=>r.createdAt
                  ? new Date(r.createdAt).toLocaleString("en-IN",
                      {day:"numeric",month:"short",year:"numeric",
                       hour:"2-digit",minute:"2-digit"})
                  : "—"
              },
            ]}/>
          )}
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}

/* ─── AUDIT MODULE ────────────────────────────────────────── */
function AuditModule() {
  const [tab, setTab] = useState("generate");
  const [toast, notify, setToast] = useToast();

  /* generate */
  const [genForm, setGenForm] = useState({reportName:"", generatedBy:""});
  const [genLoad, setGenLoad] = useState(false);
  const [genRes, setGenRes]   = useState(null);   // ← ComplianceReport

  /* reports */
  const [reports, setReports] = useState([]);
  const [repLoad, setRepLoad] = useState(false);

  /* logs */
  const [logs, setLogs]         = useState([]);
  const [logsLoad, setLogsLoad] = useState(false);

  /* log by ID */
  const [auditId, setAuditId]     = useState("");
  const [singleLog, setSingleLog] = useState(null); // ← AuditLog
  const [singleLoad, setSingleLoad] = useState(false);

  /* export */
  const [exportLoad, setExportLoad] = useState(false);

  /* helper — format LocalDateTime nicely */
  const fmtDt = v => v
    ? new Date(v).toLocaleString("en-IN",{
        day:"numeric",month:"short",year:"numeric",
        hour:"2-digit",minute:"2-digit"})
    : "—";

  /* ─── GENERATE REPORT ─── */
  const genRep = async () => {
    if (!genForm.reportName) return notify("Report Name is required","error");

    setGenLoad(true);
    setGenRes(null);
    try {
      const res = await API.post("/compliance/reports/generate", {
        reportName:  genForm.reportName,
        generatedBy: genForm.generatedBy || "admin",
      });
      setGenRes(res.data);                  // ← ComplianceReport entity
      notify("Report generated successfully");
      setGenForm({reportName:"", generatedBy:""});
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Generation failed";
      notify(msg,"error");
    } finally { setGenLoad(false); }
  };

  /* ─── GET ALL REPORTS ─── */
  const getReps = async () => {
    setRepLoad(true);
    try {
      const res = await API.get("/compliance/reports");
      setReports(Array.isArray(res.data)?res.data:[]);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Failed to load reports";
      notify(msg,"error");
      setReports([]);
    } finally { setRepLoad(false); }
  };

  /* ─── GET ALL LOGS ─── */
  const getLogs = async () => {
    setLogsLoad(true);
    try {
      const res = await API.get("/audit/logs");
      setLogs(Array.isArray(res.data)?res.data:[]);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Failed to load logs";
      notify(msg,"error");
      setLogs([]);
    } finally { setLogsLoad(false); }
  };

  /* ─── GET LOG BY ID ─── */
  const getLogById = async () => {
    if (!auditId.trim()) return notify("Enter Audit ID","error");
    const id = Number(auditId);
    if (Number.isNaN(id)) return notify("Audit ID must be a number","error");

    setSingleLoad(true);
    setSingleLog(null);
    try {
      const res = await API.get(`/audit/logs/${id}`);
      setSingleLog(res.data||null);
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Audit log not found";
      notify(msg,"error");
    } finally { setSingleLoad(false); }
  };

  /* ─── EXPORT CSV ─── */
  const exportLogs = async () => {
    setExportLoad(true);
    try {
      const res = await API.post("/audit/logs/export", {}, {responseType:"blob"});
      const url = URL.createObjectURL(new Blob([res.data],{type:"text/csv"}));
      const a   = document.createElement("a");
      a.href     = url;
      a.download = "audit_report.csv";
      a.click();
      URL.revokeObjectURL(url);
      notify("Audit logs exported successfully");
    } catch(e) {
      const msg = e?.response?.data?.message||e.message||"Export failed";
      notify(msg,"error");
    } finally { setExportLoad(false); }
  };

  const TABS = [
    {id:"generate", label:"Generate Report", icon:D.plus},
    {id:"reports",  label:"All Reports",     icon:D.doc},
    {id:"logs",     label:"All Logs",         icon:D.log},
    {id:"logById",  label:"Log by ID",        icon:D.search},
  ];

  return (
    <div style={{padding:28}}>
      <PH icon={D.shield} title="Audit & Compliance"
        sub="Generate reports and review audit logs"
        bg="#FFFBEB" color="#B45309"/>
      <Tabs tabs={TABS} active={tab} setActive={setTab} color="#B45309"/>

      {/* ─── GENERATE ─── */}
      {tab==="generate" && (
        <Card title="Generate Audit Report" icon={D.plus} color="#B45309">
          <Fld label="Report Name" required>
            <FInput value={genForm.reportName}
              onChange={e=>setGenForm(p=>({...p,reportName:e.target.value}))}
              placeholder="Q1 Compliance Report 2025"/>
          </Fld>
          <Fld label="Generated By">
            <FInput value={genForm.generatedBy}
              onChange={e=>setGenForm(p=>({...p,generatedBy:e.target.value}))}
              placeholder="Admin / Compliance Officer"/>
          </Fld>

          <div style={{display:"flex",gap:10}}>
            <Btn loading={genLoad} color="#B45309" icon={D.plus} onClick={genRep}>
              Generate Report
            </Btn>
            <Btn variant="secondary" onClick={()=>{
              setGenForm({reportName:"",generatedBy:""});
              setGenRes(null);
            }}>
              Reset
            </Btn>
          </div>

          {/* ── RESPONSE — generated ComplianceReport ── */}
          <ResponseCard
            title="Report Generated Successfully"
            data={genRes ? {
              ...genRes,
              generatedAt: fmtDt(genRes.generatedAt),  // format datetime
            } : null}
            color="#B45309"
            onClose={()=>setGenRes(null)}
          />
        </Card>
      )}

      {/* ─── ALL REPORTS ─── */}
      {tab==="reports" && (
        <Card title="Compliance Reports" icon={D.doc} color="#B45309"
          action={
            <Btn onClick={getReps} loading={repLoad} color="#B45309">
              {reports.length?"Refresh":"Load"}
            </Btn>
          }>
          {!repLoad && reports.length===0 && (
            <Empty emoji="📋" label="No reports loaded"
              hint='Click "Load" to fetch compliance reports'/>
          )}
          {repLoad && (
            <div style={{textAlign:"center",padding:32,color:"#94A3B8"}}>
              Loading reports…
            </div>
          )}
          {reports.length>0 && (
            <DT cols={[
              {k:"id",          l:"ID"},
              {k:"reportName",  l:"Name"},
              {k:"generatedBy", l:"Generated By"},
              {k:"generatedAt", l:"Generated At",
                r:r=>fmtDt(r.generatedAt)},
              {k:"status",      l:"Status",
                r:r=><Badge status={r.status||"COMPLETED"}/>},
            ]} rows={reports}/>
          )}
        </Card>
      )}

      {/* ─── ALL LOGS ─── */}
      {tab==="logs" && (
        <Card title="Audit Logs" icon={D.log} color="#B45309"
          action={
            <div style={{display:"flex",gap:8}}>
              <Btn onClick={getLogs} loading={logsLoad} color="#B45309">
                {logs.length?"Refresh":"Load"}
              </Btn>
              <Btn onClick={exportLogs} loading={exportLoad}
                color="#B45309" icon={D.dl}>
                Export CSV
              </Btn>
            </div>
          }>
          {!logsLoad && logs.length===0 && (
            <Empty emoji="📜" label="No logs loaded"
              hint='Click "Load" to fetch audit logs'/>
          )}
          {logsLoad && (
            <div style={{textAlign:"center",padding:32,color:"#94A3B8"}}>
              Loading logs…
            </div>
          )}
          {logs.length>0 && (
            <DT cols={[
              {k:"id",          l:"ID"},
              {k:"module",      l:"Module"},
              {k:"action",      l:"Action"},
              {k:"performedBy", l:"Performed By"},
              {k:"resourceId",  l:"Resource ID"},
              {k:"timestamp",   l:"Timestamp",
                r:r=>fmtDt(r.timestamp)},
            ]} rows={logs}/>
          )}
        </Card>
      )}

      {/* ─── LOG BY ID ─── */}
      {tab==="logById" && (
        <Card title="Audit Log by ID" icon={D.search} color="#B45309">
          <Fld label="Audit ID">
            <FInput value={auditId}
              onChange={e=>setAuditId(e.target.value)}
              placeholder="Enter numeric audit log ID"
              icon={D.search}
              onKeyDown={e=>e.key==="Enter"&&getLogById()}/>
          </Fld>
          <Btn loading={singleLoad} color="#B45309" icon={D.search}
            onClick={getLogById}>
            Fetch Log
          </Btn>

          {/* ── RESPONSE — full AuditLog record ── */}
          <ResponseCard
            title="Audit Log Found"
            data={singleLog ? {
              ...singleLog,
              timestamp: fmtDt(singleLog.timestamp),  // format datetime
            } : null}
            color="#B45309"
            onClose={()=>setSingleLog(null)}
          />
        </Card>
      )}

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── ROLE REGISTRATION MODULE ────────────────────────────── */
const ROLES = [
  "ADMIN",
  "DOCTOR",
  "RECEPTION",
  "NURSE",
  "BILLING",
  "COMPLIANCE_OFFICER"
];

const RR0 = {
  username:"", password:"", role:"",
  email:"", phoneNumber:""
};

function RoleRegistrationModule() {
  const [form, setForm]   = useState(RR0);
  const [loading, setLoading] = useState(false);
  const [toast, notify, setToast] = useToast();
  const [showPw, setShowPw]   = useState(false);
  const [registerRes, setRegisterRes] = useState(null); // ← registration confirmation

  const onChange = key => e => setForm(prev=>({...prev,[key]:e.target.value}));

  /* ─── REGISTER ─── */
  const register = async () => {
    if (!form.username||!form.password||!form.role||!form.email||!form.phoneNumber)
      return notify("Fill all required fields","error");

    // Password min 8 chars — backend @Size(min=8)
    if (form.password.length < 8)
      return notify("Password must be at least 8 characters","error");

    // Strip +91 / spaces / dashes — backend expects ^[6-9][0-9]{9}$
    const cleanPhone = form.phoneNumber
      .replace(/^\+91/,"").replace(/\s+/g,"").replace(/-/g,"");

    if (!/^[6-9][0-9]{9}$/.test(cleanPhone))
      return notify("Phone must be a valid 10-digit Indian number (e.g. 9876543210)","error");

    setLoading(true);
    setRegisterRes(null);
    try {
      await API.post("/auth/register", {...form, phoneNumber: cleanPhone});
      // register returns void — build confirmation object from form values
      setRegisterRes({
        username: form.username,
        role:     form.role.replace(/_/g," "),
        email:    form.email,
        phone:    cleanPhone,
        status:   "Account created successfully",
      });
      notify(`${form.role} account created for ${form.username}`);
      setForm(RR0);
    } catch(e) {
      const msg =
        e?.response?.data?.message ||
        e?.response?.data?.messages?.phoneNumber ||
        e?.response?.data?.messages?.password    ||
        e?.response?.data?.messages?.email       ||
        e?.response?.data?.messages?.username    ||
        e?.message || "Registration failed";
      notify(msg,"error");
    } finally { setLoading(false); }
  };

  return (
    <div style={{padding:28}}>
      <PH icon={D.users} title="Role Registration"
        sub="Register new staff members with specific roles"
        bg="#FDF2F8" color="#BE185D"/>

      <Card title="Register New Staff Member" icon={D.plus} color="#BE185D">
        <p style={{fontSize:13,color:"#64748B",marginBottom:20}}>
          Create accounts for doctors, nurses, receptionists and other staff.
          Role-based access is enforced by the backend.
        </p>

        <div style={{display:"grid",gridTemplateColumns:"1fr 1fr",gap:"0 24px"}}>
          <Fld label="Username" required>
            <FInput value={form.username} onChange={onChange("username")}
              placeholder="e.g. dr.smith" icon={D.search}/>
          </Fld>

          <Fld label="Role" required>
            <FSel value={form.role} onChange={onChange("role")}>
              <option value="">Select role</option>
              {ROLES.map(r=>(
                <option key={r} value={r}>{r.replace(/_/g," ")}</option>
              ))}
            </FSel>
          </Fld>

          <Fld label="Password" required>
            <div style={{position:"relative"}}>
              <FInput value={form.password} onChange={onChange("password")}
                type={showPw?"text":"password"}
                placeholder="Min. 8 characters"
                icon={D.lock}/>
              <button type="button" onClick={()=>setShowPw(v=>!v)}
                style={{position:"absolute",right:12,top:"50%",
                  transform:"translateY(-50%)",background:"none",border:"none",
                  cursor:"pointer",color:"#94A3B8",fontSize:18}}>
                {showPw?"🙈":"👁"}
              </button>
            </div>
          </Fld>

          <Fld label="Email Address" required>
            <FInput value={form.email} onChange={onChange("email")}
              placeholder="staff@hospital.com" type="email" icon={D.mail}/>
          </Fld>

          <Fld label="Phone Number" required>
            <FInput value={form.phoneNumber} onChange={onChange("phoneNumber")}
              placeholder="9876543210 (10 digits, no +91)" type="tel"/>
          </Fld>
        </div>

        {/* role info banner */}
        {form.role && (
          <div style={{background:"#FDF2F8",border:"1px solid #FBCFE8",
            borderRadius:10,padding:"12px 16px",marginBottom:16,
            display:"flex",alignItems:"center",gap:10}}>
            <Ic d={D.shield} size={16}/>
            <span style={{fontSize:13,color:"#9D174D",fontWeight:500}}>
              Role: <strong>{form.role.replace(/_/g," ")}</strong> — permissions
              will be enforced at the backend level.
            </span>
          </div>
        )}

        <div style={{display:"flex",gap:10,marginTop:8}}>
          <Btn color="#BE185D" icon={D.users} loading={loading} onClick={register}>
            Register Staff Member
          </Btn>
          <Btn variant="secondary" onClick={()=>{setForm(RR0);setRegisterRes(null);}}>
            Reset
          </Btn>
        </div>

        {/* ── RESPONSE — registration confirmation ── */}
        <ResponseCard
          title="Staff Member Registered"
          data={registerRes}
          color="#BE185D"
          onClose={()=>setRegisterRes(null)}
        />
      </Card>

      <Toast msg={toast?.msg} type={toast?.type} onClose={()=>setToast(null)}/>
    </div>
  );
}
/* ─── DOCTOR APPLICATIONS (placeholder) ──────────────────── */
function PlaceholderModule({mod}){
  return(
    <div style={{padding:32}}>
      <PH icon={MOD_ICONS[mod.id]} title={mod.label} sub={mod.desc} bg={mod.bg} color={mod.color}/>
      <div style={{background:"#fff",borderRadius:16,border:"1.5px dashed #CBD5E1",padding:"64px 32px",textAlign:"center"}}>
        <div style={{width:64,height:64,borderRadius:16,background:mod.bg,margin:"0 auto 20px",display:"flex",alignItems:"center",justifyContent:"center",color:mod.color}}><Ic d={MOD_ICONS[mod.id]} size={32}/></div>
        <h3 style={{fontFamily:"'Syne',sans-serif",fontSize:18,fontWeight:700,color:"#1E293B",marginBottom:8}}>{mod.label}</h3>
        <p style={{fontSize:14,color:"#94A3B8",maxWidth:380,margin:"0 auto"}}>Awaiting specifications. Describe the required tasks and this module will be fully built.</p>
      </div>
    </div>
  );
}

/* ─── MODULE REGISTRY ─────────────────────────────────────── */
const MOD_ICONS={
  doctor:      "M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z",
  patient:     "M16 11c0 2.21-1.79 4-4 4s-4-1.79-4-4 1.79-4 4-4 4 1.79 4 4zm-8 8v-1a4 4 0 014-4h0a4 4 0 014 4v1",
  slot:        "M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z",
  appointment: "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2",
  applications:"M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z",
  analytics:   "M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z",
  billing:     "M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z",
  roles:       "M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z",
  audit:       "M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z",
  ward:        "M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4",
  notification:"M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9",
};

const MODULES=[
  {id:"doctor",       label:"Doctor Services",       color:"#3B82F6",bg:"#EFF6FF",desc:"Manage doctor profiles & schedules",     badge:null},
  {id:"patient",      label:"Patient Management",    color:"#8B5CF6",bg:"#F5F3FF",desc:"Patient records & medical history",       badge:null},
  {id:"slot",         label:"Slot Services",          color:"#0891B2",bg:"#ECFEFF",desc:"Manage availability slots & timing",      badge:null},
  {id:"appointment",  label:"Appointment Services",  color:"#059669",bg:"#ECFDF5",desc:"Book, reschedule & track appointments",   badge:null},
  {id:"applications", label:"Doctor Applications",   color:"#D97706",bg:"#FFFBEB",desc:"Review & approve doctor applications",    badge:"7 Pending"},
  {id:"analytics",    label:"Analytics & KPI",       color:"#7C3AED",bg:"#F5F3FF",desc:"Calendar insights & reports",             badge:"Live Data"},
  {id:"billing",      label:"Billing Services",      color:"#0F766E",bg:"#F0FDFA",desc:"Invoices, claims & payments",             badge:null},
  {id:"roles",        label:"Role Registrations",    color:"#BE185D",bg:"#FDF2F8",desc:"Register staff with specific roles",      badge:"6 Roles"},
  {id:"audit",        label:"Audit & Compliance",    color:"#B45309",bg:"#FFFBEB",desc:"Audit logs, compliance & reporting",      badge:"HIPAA Ready"},
  {id:"ward",         label:"Ward Services",          color:"#1D4ED8",bg:"#EFF6FF",desc:"Bed blueprint, admissions & discharge",   badge:null},
  {id:"notification", label:"Notification Service",  color:"#DC2626",bg:"#FEF2F2",desc:"Alerts, emails, SMS & notices",           badge:null},
];

const MC={
  doctor:DoctorModule, patient:PatientModule, slot:SlotModule,
  appointment:AppointmentModule, analytics:AnalyticsModule, billing:BillingModule,
  ward:WardModule, notification:NotificationModule, audit:AuditModule, roles:RoleRegistrationModule,
};

/* ─── OVERVIEW GRID ───────────────────────────────────────── */
function Overview({onSelect}){
  const [hov,sh]=useState(null);
  const [stats,setStats]=useState({patients:null,appointments:null,activeAdmissions:null,revenue:null});
  const [loading,setLoading]=useState(true);

  const [modStats,setModStats]=useState({
    patient:null, appointment:null, ward:null, billing:null, notification:null
  });

  useEffect(()=>{
    const today=new Date().toISOString().split("T")[0];
    const safe=async(fn,fallback=null)=>{
      try{ const r=await fn(); return r.data??r; }
      catch{ return fallback; }
    };

    // ── KPI stat cards ──
    Promise.all([
      safe(()=>API.get("/patients/count"),0),
      safe(()=>API.get(`/appointments/count/by-date?date=${today}`),0),
      safe(()=>API.get("/wards/admissions/active"),0),
      safe(()=>API.get(`/invoices/revenue/by-date?date=${today}`),0.0),
    ]).then(([patients,appointments,activeAdmissions,revenue])=>{
      setStats({patients,appointments,activeAdmissions,revenue});
      setLoading(false);
    });

    // ── Module card badges ──
    Promise.all([
      safe(()=>API.get("/patients/count")),
      safe(()=>API.get(`/appointments/count/by-date?date=${today}`)),
      safe(()=>API.get("/wards/admissions/active")),
      safe(()=>API.get(`/invoices/revenue/by-date?date=${today}`)),
    ]).then(([patients,appointments,activeWard,revenue])=>{
      const fmtRev=(n)=>{
        if(n===null||n===undefined)return null;
        const num=Number(n);
        if(num>=100000)return "₹"+(num/100000).toFixed(1)+"L Today";
        if(num>0)return "₹"+num.toLocaleString("en-IN")+" Today";
        return null;
      };
      setModStats({
        patient:   patients!==null   ? `${patients} Total`      : null,
        appointment: appointments!==null ? `${appointments} Today` : null,
        ward:      activeWard!==null  ? `${activeWard} Active`   : null,
        billing:   fmtRev(revenue),
        notification: null,
      });
    });
  },[]);

  const fmt=(n)=>{
    if(n===null)return "—";
    if(typeof n==="number"){
      if(n>=100000)return "₹"+(n/100000).toFixed(1)+"L";
      if(n>=1000)return n.toLocaleString("en-IN");
      return String(n);
    }
    return String(n);
  };

  const STAT_CARDS=[
    {l:"Total Patients",    v:fmt(stats.patients),         s:"All registered patients",  c:"#3B82F6", icon:D.users},
    {l:"Appointments Today",v:fmt(stats.appointments),     s:new Date().toLocaleDateString("en-IN",{day:"numeric",month:"short"}), c:"#059669", icon:D.cal},
    {l:"Active Admissions", v:fmt(stats.activeAdmissions), s:"Currently admitted",        c:"#8B5CF6", icon:D.ward},
    {l:"Revenue Today",     v:fmt(stats.revenue),          s:new Date().toLocaleDateString("en-IN",{day:"numeric",month:"short"}), c:"#0F766E", icon:D.money},
  ];

  return(
    <div style={{padding:32}}>

      {/* ── STAT CARDS ── */}
      <div style={{display:"grid",gridTemplateColumns:"repeat(4,1fr)",gap:16,marginBottom:36}}>
        {STAT_CARDS.map(s=>(
          <div key={s.l} style={{background:"#fff",borderRadius:14,border:"1px solid #E2E8F0",padding:"20px 22px",boxShadow:"0 1px 4px rgba(0,0,0,.04)",position:"relative",overflow:"hidden"}}>
            <div style={{position:"absolute",top:16,right:16,width:34,height:34,borderRadius:9,background:s.c+"18",display:"flex",alignItems:"center",justifyContent:"center",color:s.c}}>
              <Ic d={s.icon} size={17}/>
            </div>
            <div style={{fontSize:11,color:"#94A3B8",fontWeight:700,letterSpacing:"0.05em",marginBottom:10,textTransform:"uppercase"}}>{s.l}</div>
            {loading
              ? <div style={{height:32,width:80,background:"#F1F5F9",borderRadius:8,marginBottom:6}}/>
              : <div style={{fontFamily:"'Syne',sans-serif",fontSize:28,fontWeight:700,color:s.c,lineHeight:1}}>{s.v}</div>
            }
            <div style={{fontSize:12,color:"#64748B",marginTop:6}}>{s.s}</div>
          </div>
        ))}
      </div>

      {/* ── MODULE GRID ── */}
      <h2 style={{fontFamily:"'Syne',sans-serif",fontSize:18,fontWeight:700,color:"#0F172A",margin:"0 0 4px"}}>Service Modules</h2>
      <p style={{fontSize:13,color:"#94A3B8",margin:"0 0 20px"}}>Click any module to open and manage</p>
      <div style={{display:"grid",gridTemplateColumns:"repeat(auto-fill,minmax(210px,1fr))",gap:16}}>
        {MODULES.map(m=>(
          <div key={m.id} onClick={()=>onSelect(m.id)} onMouseEnter={()=>sh(m.id)} onMouseLeave={()=>sh(null)}
            style={{background:"#fff",borderRadius:16,padding:22,cursor:"pointer",border:`1.5px solid ${hov===m.id?m.color:"#E2E8F0"}`,transition:"all .22s",boxShadow:hov===m.id?`0 8px 28px ${m.color}22`:"0 1px 4px rgba(0,0,0,.04)",transform:hov===m.id?"translateY(-3px)":"none"}}>
            <div style={{width:46,height:46,borderRadius:12,background:m.bg,display:"flex",alignItems:"center",justifyContent:"center",color:m.color,marginBottom:14,transition:"transform .2s",transform:hov===m.id?"scale(1.1)":"scale(1)"}}>
              <Ic d={MOD_ICONS[m.id]} size={22}/>
            </div>
            <div style={{fontFamily:"'Syne',sans-serif",fontSize:14,fontWeight:700,color:"#1E293B",marginBottom:5}}>{m.label}</div>
            <div style={{fontSize:12,color:"#94A3B8",marginBottom:14,lineHeight:1.55}}>{m.desc}</div>
            <div style={{display:"flex",alignItems:"center",justifyContent:"space-between"}}>
              <span style={{fontSize:11,fontWeight:700,color:m.color,background:m.bg,padding:"3px 10px",borderRadius:20}}>
  {modStats[m.id]??m.badge??"—"}
</span>
              <span style={{color:hov===m.id?m.color:"#CBD5E1",transition:"color .2s",display:"flex"}}><Ic d={D.chev} size={16}/></span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
/* ─── ROOT DASHBOARD ──────────────────────────────────────── */
export default function AdminDashboard(){
  const [active,setActive]=useState(null);
  const [open,setOpen]=useState(true);
  const [hov,sh]=useState(null);
  const mod=MODULES.find(m=>m.id===active);
  const Comp=active?MC[active]:null;

  return(
    <>
      <style>{GS}</style>
      <div style={{display:"flex",height:"100vh",overflow:"hidden"}}>

        {/* SIDEBAR */}
        <div style={{width:open?242:68,background:"linear-gradient(180deg,#0A1628 0%,#0D2145 100%)",display:"flex",flexDirection:"column",transition:"width .25s ease",overflow:"hidden",flexShrink:0,zIndex:10}}>
          <div style={{padding:"18px 14px",display:"flex",alignItems:"center",gap:10,borderBottom:"1px solid rgba(255,255,255,.07)",flexShrink:0}}>
            <div style={{width:36,height:36,borderRadius:10,flexShrink:0,background:"rgba(0,201,167,.15)",border:"1px solid rgba(0,201,167,.3)",display:"flex",alignItems:"center",justifyContent:"center"}}>
              <svg width="18" height="18" viewBox="0 0 28 28" fill="none"><path d="M2 14h4l3-9 4 18 3-12 2 6 2-3h6" stroke="#00C9A7" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/></svg>
            </div>
            {open&&<div><div style={{fontFamily:"'Syne',sans-serif",fontSize:14,fontWeight:700,color:"#fff",whiteSpace:"nowrap"}}>HEALTHCONNECT</div><div style={{fontSize:10,color:"#00C9A7",letterSpacing:"0.05em",fontWeight:600}}>ADMIN PORTAL</div></div>}
          </div>

          <div style={{flex:1,overflowY:"auto",overflowX:"hidden",padding:"10px 8px"}}>
            <div className={`sbi ${active===null?"act":""}`} onClick={()=>setActive(null)}>
              <div style={{width:32,height:32,borderRadius:9,flexShrink:0,background:active===null?"rgba(0,201,167,.18)":"transparent",display:"flex",alignItems:"center",justifyContent:"center",color:active===null?"#00C9A7":"#7E9EC4"}}><Ic d={D.home} size={17}/></div>
              {open&&<span style={{fontSize:13,fontWeight:600,color:active===null?"#fff":"#94A3B8",whiteSpace:"nowrap"}}>Overview</span>}
            </div>
            {open&&<div style={{fontSize:10,color:"#3D5A80",fontWeight:700,letterSpacing:"0.08em",padding:"12px 6px 5px",textTransform:"uppercase"}}>Modules</div>}
            {MODULES.map(m=>{
              const act=active===m.id;
              const badge=(m.id==="applications"&&"7")||(m.id==="notification"&&"3")||null;
              return(
                <div key={m.id} className={`sbi ${act?"act":""}`} onClick={()=>setActive(m.id)} onMouseEnter={()=>sh(m.id)} onMouseLeave={()=>sh(null)} style={{position:"relative"}}>
                  <div style={{width:32,height:32,borderRadius:9,flexShrink:0,background:act?`${m.color}22`:"transparent",display:"flex",alignItems:"center",justifyContent:"center",color:act?m.color:"#7E9EC4",transition:"all .15s"}}><Ic d={MOD_ICONS[m.id]} size={17}/></div>
                  {open&&<span style={{fontSize:13,fontWeight:act?600:400,color:act?"#fff":"#94A3B8",whiteSpace:"nowrap",flex:1}}>{m.label}</span>}
                  {open&&badge&&<span style={{background:"#EF4444",color:"#fff",fontSize:10,fontWeight:700,padding:"2px 6px",borderRadius:10,flexShrink:0}}>{badge}</span>}
                  {!open&&hov===m.id&&<div style={{position:"absolute",left:48,top:"50%",transform:"translateY(-50%)",background:"#1E293B",color:"#fff",fontSize:12,fontWeight:600,padding:"6px 12px",borderRadius:8,whiteSpace:"nowrap",zIndex:100,boxShadow:"0 4px 16px rgba(0,0,0,.3)",pointerEvents:"none"}}>{m.label}</div>}
                </div>
              );
            })}
          </div>

          <div style={{padding:"12px 8px",borderTop:"1px solid rgba(255,255,255,.07)",flexShrink:0}}>
            <div className="sbi" style={{marginBottom:0}}>
              <div style={{width:32,height:32,borderRadius:"50%",flexShrink:0,background:"linear-gradient(135deg,#3B82F6,#8B5CF6)",display:"flex",alignItems:"center",justifyContent:"center",fontSize:12,fontWeight:700,color:"#fff"}}>AD</div>
              {open&&<><div style={{flex:1,minWidth:0}}><div style={{fontSize:12,fontWeight:600,color:"#fff",whiteSpace:"nowrap"}}>Admin User</div><div style={{fontSize:11,color:"#7E9EC4",whiteSpace:"nowrap"}}>System Administrator</div></div><div style={{color:"#7E9EC4",cursor:"pointer"}} onClick={()=>window.location.href="/login"}><Ic d={D.logout} size={15}/></div></>}
            </div>
          </div>
        </div>

        {/* MAIN */}
        <div style={{flex:1,display:"flex",flexDirection:"column",overflow:"hidden"}}>
          <div style={{height:60,background:"#fff",borderBottom:"1px solid #E2E8F0",display:"flex",alignItems:"center",padding:"0 22px",gap:10,flexShrink:0}}>
            <button className="tbb" onClick={()=>setOpen(v=>!v)}><Ic d={D.menu} size={18}/></button>
            <div style={{display:"flex",alignItems:"center",gap:6,fontSize:13}}>
              <span style={{cursor:"pointer",color:active?"#94A3B8":"#1E293B",fontWeight:active?400:600}} onClick={()=>setActive(null)}>Admin</span>
              {mod&&<><Ic d={D.chev} size={14}/><span style={{color:mod.color,fontWeight:700}}>{mod.label}</span></>}
            </div>
            <div style={{display:"flex",alignItems:"center",gap:8,background:"#F8FAFC",border:"1px solid #E2E8F0",borderRadius:9,padding:"7px 14px",marginLeft:"auto",width:240}}>
              <Ic d={D.search} size={15}/><input placeholder="Search…" style={{border:"none",background:"transparent",fontSize:13,color:"#1E293B",outline:"none",width:"100%",fontFamily:"'DM Sans',sans-serif"}}/>
            </div>
            <div style={{position:"relative"}}><button className="tbb"><Ic d={D.bell} size={18}/></button><div style={{position:"absolute",top:4,right:4,width:8,height:8,borderRadius:"50%",background:"#EF4444",border:"2px solid #fff"}}/></div>
            <div style={{width:36,height:36,borderRadius:"50%",background:"linear-gradient(135deg,#3B82F6,#8B5CF6)",display:"flex",alignItems:"center",justifyContent:"center",fontSize:13,fontWeight:700,color:"#fff",cursor:"pointer",flexShrink:0}}>AD</div>
          </div>
          <div style={{flex:1,overflowY:"auto"}} className="page" key={active}>
            {Comp ? <Comp/> : active ? <PlaceholderModule mod={mod}/> : <Overview onSelect={setActive}/>}
          </div>
        </div>
      </div>
    </>
  );
}
