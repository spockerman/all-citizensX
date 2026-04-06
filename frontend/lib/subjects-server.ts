import { auth } from "@/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:9080";

type ApiSubject = {
  id: string;
  tenantId: string;
  departmentId: string | null;
  name: string;
  active: boolean;
  visibleWeb: boolean;
  visibleApp: boolean;
  createdAt: string | null;
  updatedAt: string | null;
};

type PageResponse = {
  content: ApiSubject[];
  totalElements: number;
};

function formatInstant(iso: string | null | undefined) {
  if (!iso) return "—";
  try {
    return new Intl.DateTimeFormat("en-US", {
      dateStyle: "short",
      timeStyle: "short",
    }).format(new Date(iso));
  } catch {
    return "—";
  }
}

export type SubjectRow = {
  id: string;
  name: string;
  active: boolean;
  visibleWeb: boolean;
  visibleApp: boolean;
  createdLabel: string;
  updatedLabel: string;
};

export type SubjectDetail = {
  id: string;
  name: string;
  departmentId: string | null;
  active: boolean;
  visibleWeb: boolean;
  visibleApp: boolean;
};

export type FetchSubjectsListResult =
  | { ok: true; rows: SubjectRow[]; total: number }
  | {
      ok: false;
      reason: "no_token" | "no_tenant" | "upstream";
      status?: number;
    };

export type FetchSubjectOneResult =
  | { ok: true; subject: SubjectDetail }
  | {
      ok: false;
      reason: "no_token" | "not_found" | "upstream";
      status?: number;
    };

function mapListRow(r: ApiSubject): SubjectRow {
  return {
    id: r.id,
    name: r.name,
    active: r.active,
    visibleWeb: r.visibleWeb,
    visibleApp: r.visibleApp,
    createdLabel: formatInstant(r.createdAt),
    updatedLabel: formatInstant(r.updatedAt),
  };
}

function mapDetail(r: ApiSubject): SubjectDetail {
  return {
    id: r.id,
    name: r.name,
    departmentId: r.departmentId,
    active: r.active,
    visibleWeb: r.visibleWeb,
    visibleApp: r.visibleApp,
  };
}

export async function fetchSubjectsList(
  page: number,
  size: number,
): Promise<FetchSubjectsListResult> {
  const session = await auth();
  if (!session?.accessToken) {
    return { ok: false, reason: "no_token" };
  }

  const tenantId = process.env.TENANT_ID;
  if (!tenantId) {
    return { ok: false, reason: "no_tenant" };
  }

  const url = new URL(`${API_URL}/api/v1/subjects`);
  url.searchParams.set("tenantId", tenantId);
  url.searchParams.set("page", String(page));
  url.searchParams.set("size", String(size));

  const res = await fetch(url.toString(), {
    headers: { Authorization: `Bearer ${session.accessToken}` },
    cache: "no-store",
  });

  if (!res.ok) {
    return { ok: false, reason: "upstream", status: res.status };
  }

  const data = (await res.json()) as PageResponse;
  const rows = (data.content ?? []).map(mapListRow);

  return {
    ok: true,
    rows,
    total: data.totalElements ?? rows.length,
  };
}

export async function fetchSubjectById(id: string): Promise<FetchSubjectOneResult> {
  const session = await auth();
  if (!session?.accessToken) {
    return { ok: false, reason: "no_token" };
  }

  const res = await fetch(`${API_URL}/api/v1/subjects/${encodeURIComponent(id)}`, {
    headers: { Authorization: `Bearer ${session.accessToken}` },
    cache: "no-store",
  });

  if (res.status === 404) {
    return { ok: false, reason: "not_found", status: 404 };
  }

  if (!res.ok) {
    return { ok: false, reason: "upstream", status: res.status };
  }

  const raw = (await res.json()) as ApiSubject;
  return { ok: true, subject: mapDetail(raw) };
}

export function activeBadgeClass(active: boolean) {
  return active
    ? "bg-emerald-50 text-emerald-900 border-emerald-200"
    : "bg-gray-100 text-gray-600 border-gray-200";
}
