import { SubjectForm } from "@/components/dashboard/subject-form";
import { fetchSubjectById } from "@/lib/subjects-server";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Edit subject — All Citizens",
};

type Props = { params: Promise<{ id: string }> };

function editFetchHint(
  result: Awaited<ReturnType<typeof fetchSubjectById>>,
): string | null {
  if (result.ok) return null;
  switch (result.reason) {
    case "no_token":
      return "The session has no Keycloak access token. Sign out and sign in again.";
    case "not_found":
      return "Subject not found or not allowed to view.";
    case "upstream":
      return `Could not load the subject (HTTP ${result.status ?? "—"}).`;
    default:
      return null;
  }
}

export default async function EditSubjectPage({ params }: Props) {
  const { id } = await params;
  const result = await fetchSubjectById(id);
  const tenantConfigured = Boolean(process.env.TENANT_ID);
  const hint = editFetchHint(result);

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
          <span className="text-on-surface">Edit</span>
        </div>
        <h1 className="text-2xl font-bold tracking-tight text-on-surface">
          Edit subject
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Update name, visibility, and status.
        </p>
      </div>

      {hint ? (
        <div className="mb-6 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-950">
          {hint}
        </div>
      ) : null}

      {result.ok ? (
        <SubjectForm
          mode="edit"
          tenantConfigured={tenantConfigured}
          initial={result.subject}
        />
      ) : (
        <div className="rounded-xl border border-gray-100 bg-white px-6 py-12 text-center text-sm text-gray-500">
          <Link
            href="/dashboard/subjects"
            className="font-semibold text-on-surface underline-offset-2 hover:underline"
          >
            Back to subjects
          </Link>
        </div>
      )}
    </>
  );
}
