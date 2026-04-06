import { auth } from "@/auth";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:9080";

type ApiRow = {
  id: string;
  protocol: string;
  channel: string | null;
  status: string | null;
  description: string | null;
  updatedAt: string | null;
  createdAt: string | null;
};

type PageResponse = {
  content: ApiRow[];
  totalElements: number;
};

const CHANNEL_LABELS: Record<string, string> = {
  PHONE: "Phone",
  WEB: "Web",
  MOBILE_APP: "Mobile app",
  WHATSAPP: "WhatsApp",
  CHATBOT: "Chatbot",
  IN_PERSON: "In person",
  EMAIL: "Email",
};

const STATUS_LABELS: Record<string, string> = {
  OPEN: "Open",
  IN_PROGRESS: "In progress",
  FORWARDED: "Forwarded",
  ANSWERED: "Answered",
  CLOSED: "Closed",
  CANCELLED: "Cancelled",
  REOPENED: "Reopened",
};

function channelLabel(code: string | null | undefined) {
  if (!code) return null;
  return CHANNEL_LABELS[code] ?? code;
}

function statusLabel(code: string | null | undefined) {
  if (!code) return null;
  return STATUS_LABELS[code] ?? code;
}

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

export type ServiceRequestRow = {
  id: string;
  protocol: string;
  description: string | null;
  channelCode: string | null;
  channelLabel: string | null;
  statusCode: string | null;
  statusLabel: string | null;
  createdLabel: string;
  updatedLabel: string;
};

export type FetchServiceRequestsResult =
  | { ok: true; rows: ServiceRequestRow[]; total: number }
  | {
      ok: false;
      reason: "no_token" | "no_tenant" | "upstream";
      status?: number;
    };

function mapRow(r: ApiRow): ServiceRequestRow {
  return {
    id: r.id,
    protocol: r.protocol,
    description: r.description,
    channelCode: r.channel,
    channelLabel: channelLabel(r.channel),
    statusCode: r.status,
    statusLabel: statusLabel(r.status),
    createdLabel: formatInstant(r.createdAt),
    updatedLabel: formatInstant(r.updatedAt),
  };
}

export async function fetchServiceRequestsList(
  page: number,
  size: number,
): Promise<FetchServiceRequestsResult> {
  const session = await auth();
  if (!session?.accessToken) {
    return { ok: false, reason: "no_token" };
  }

  const tenantId = process.env.TENANT_ID;
  if (!tenantId) {
    return { ok: false, reason: "no_tenant" };
  }

  const url = new URL(`${API_URL}/api/v1/service-requests`);
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
  const rows = (data.content ?? []).map(mapRow);

  return {
    ok: true,
    rows,
    total: data.totalElements ?? rows.length,
  };
}

export function statusBadgeClass(statusCode: string | null | undefined) {
  switch (statusCode) {
    case "OPEN":
      return "bg-amber-50 text-amber-900 border-amber-200";
    case "IN_PROGRESS":
      return "bg-blue-50 text-blue-900 border-blue-200";
    case "FORWARDED":
      return "bg-violet-50 text-violet-900 border-violet-200";
    case "ANSWERED":
      return "bg-emerald-50 text-emerald-900 border-emerald-200";
    case "CLOSED":
      return "bg-gray-100 text-gray-700 border-gray-200";
    case "CANCELLED":
      return "bg-red-50 text-red-900 border-red-200";
    case "REOPENED":
      return "bg-orange-50 text-orange-900 border-orange-200";
    default:
      return "bg-gray-50 text-gray-700 border-gray-200";
  }
}

