import { NewServiceRequestForm } from "@/components/dashboard/new-service-request-form";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "New service request — All Citizens",
};

export default function NewServiceRequestPage() {
  const tenantConfigured = Boolean(
    process.env.TENANT_ID && process.env.DEFAULT_SERVICE_ID,
  );

  return (
    <>
      <div className="mb-6">
        <div className="mb-1 flex flex-wrap items-center gap-2 text-xs font-semibold text-gray-500">
          <Link
            href="/dashboard"
            className="transition-colors hover:text-on-surface"
          >
            Dashboard
          </Link>
          <span className="text-gray-300">/</span>
          <Link
            href="/dashboard/service-requests"
            className="transition-colors hover:text-on-surface"
          >
            Service requests
          </Link>
          <span className="text-gray-300">/</span>
          <span className="text-on-surface">New</span>
        </div>
        <h1 className="text-2xl font-bold tracking-tight text-on-surface">
          New service request
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Create a protocol via{" "}
          <code className="rounded bg-gray-100 px-1 text-xs">
            /api/v1/service-requests
          </code>
          .
        </p>
      </div>

      <NewServiceRequestForm tenantConfigured={tenantConfigured} />
    </>
  );
}
