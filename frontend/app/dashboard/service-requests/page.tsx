import { ServiceRequestsTable } from "@/components/dashboard/service-requests-table";
import { fetchServiceRequestsList } from "@/lib/service-requests-server";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Service requests — All Citizens",
};

function listFetchHint(
  result: Awaited<ReturnType<typeof fetchServiceRequestsList>>,
): string | null {
  if (result.ok) return null;
  switch (result.reason) {
    case "no_token":
      return "The session has no Keycloak access token. Sign out and sign in again after configuring JWT callbacks in Auth.js.";
    case "no_tenant":
      return "Set TENANT_ID in the frontend .env to list protocols for the tenant in the API.";
    case "upstream":
      return `Could not load service requests from the API (HTTP ${result.status ?? "—"}). Check that the backend is running and the token is authorized.`;
    default:
      return null;
  }
}

export default async function ServiceRequestsPage() {
  const result = await fetchServiceRequestsList(0, 50);
  const rows = result.ok ? result.rows : [];
  const total = result.ok ? result.total : 0;
  const fetchHint = listFetchHint(result);

  return (
    <>
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <div className="mb-1 flex items-center gap-2 text-xs font-semibold text-gray-500">
            <Link
              href="/dashboard"
              className="transition-colors hover:text-on-surface"
            >
              Dashboard
            </Link>
            <span className="text-gray-300">/</span>
            <span className="text-on-surface">Service requests</span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-on-surface">
            Service requests
          </h1>
          <p className="mt-1 text-sm text-gray-500">
            Protocols for the tenant configured in the API.
          </p>
        </div>
        <Link
          href="/dashboard/service-requests/new"
          className="inline-flex items-center justify-center gap-2 rounded-lg bg-gray-900 px-4 py-2.5 text-sm font-bold text-white shadow-sm transition-opacity hover:opacity-90"
        >
          <span className="material-symbols-outlined text-[20px]">add</span>
          New service request
        </Link>
      </div>

      <ServiceRequestsTable rows={rows} total={total} fetchHint={fetchHint} />
    </>
  );
}
