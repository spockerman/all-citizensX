import { SubjectForm } from "@/components/dashboard/subject-form";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "New subject — All Citizens",
};

export default function NewSubjectPage() {
  const tenantConfigured = Boolean(process.env.TENANT_ID);

  return (
    <>
      <div className="mb-6">
        <div className="mb-1 flex items-center gap-2 text-xs font-semibold text-gray-500">
          <Link
            href="/dashboard"
            className="transition-colors hover:text-on-surface"
          >
            Dashboard
          </Link>
          <span className="text-gray-300">/</span>
          <Link
            href="/dashboard/subjects"
            className="transition-colors hover:text-on-surface"
          >
            Subjects
          </Link>
          <span className="text-gray-300">/</span>
          <span className="text-on-surface">New</span>
        </div>
        <h1 className="text-2xl font-bold tracking-tight text-on-surface">
          New subject
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Add a subject to group services in the catalog.
        </p>
      </div>

      <SubjectForm mode="create" tenantConfigured={tenantConfigured} />
    </>
  );
}
