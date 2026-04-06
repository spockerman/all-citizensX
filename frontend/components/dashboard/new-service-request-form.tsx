"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

const CHANNEL_OPTIONS = [
  { value: "WEB", label: "Web portal" },
  { value: "PHONE", label: "Phone" },
  { value: "IN_PERSON", label: "In person" },
  { value: "WHATSAPP", label: "WhatsApp" },
  { value: "MOBILE_APP", label: "Mobile app" },
  { value: "CHATBOT", label: "Chatbot" },
  { value: "EMAIL", label: "Email" },
] as const;

const PRIORITY_OPTIONS = [
  { value: "LOW", label: "Low" },
  { value: "NORMAL", label: "Normal" },
  { value: "HIGH", label: "High" },
  { value: "URGENT", label: "Urgent" },
] as const;

type Props = {
  tenantConfigured: boolean;
};

export function NewServiceRequestForm({ tenantConfigured }: Props) {
  const router = useRouter();
  const [description, setDescription] = useState("");
  const [channel, setChannel] = useState<string>("WEB");
  const [priority, setPriority] = useState<string>("NORMAL");
  const [confidential, setConfidential] = useState(false);
  const [anonymous, setAnonymous] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      const res = await fetch("/api/service-requests", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          description,
          channel,
          priority,
          confidential,
          anonymous,
        }),
      });
      const data = (await res.json()) as { error?: string; detail?: string };
      if (!res.ok) {
        setError(
          data.detail
            ? `${data.error ?? "Error"}: ${data.detail.slice(0, 400)}`
            : (data.error ?? `HTTP ${res.status}`),
        );
        return;
      }
      router.push("/dashboard/service-requests");
      router.refresh();
    } catch {
      setError("Network error while submitting the form.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="mx-auto max-w-2xl space-y-6 rounded-xl border border-gray-100 bg-white p-6 md:p-8"
    >
      {!tenantConfigured ? (
        <div className="rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-950">
          Set{" "}
          <code className="rounded bg-amber-100/80 px-1">TENANT_ID</code> and{" "}
          <code className="rounded bg-amber-100/80 px-1">DEFAULT_SERVICE_ID</code>{" "}
          in the frontend environment to create service requests via the API.
        </div>
      ) : null}

      {error ? (
        <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-900">
          {error}
        </div>
      ) : null}

      <div>
        <label
          htmlFor="description"
          className="mb-2 block text-sm font-bold text-on-surface"
        >
          Description <span className="text-red-600">*</span>
        </label>
        <textarea
          id="description"
          required
          rows={5}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Describe the request with as much detail as possible."
          className="w-full rounded-lg border border-gray-200 bg-gray-50/50 px-4 py-3 text-sm text-on-surface placeholder:text-gray-400 focus:border-accent focus:ring-1 focus:ring-accent"
        />
      </div>

      <div className="grid gap-6 sm:grid-cols-2">
        <div>
          <label
            htmlFor="channel"
            className="mb-2 block text-sm font-bold text-on-surface"
          >
            Channel
          </label>
          <select
            id="channel"
            value={channel}
            onChange={(e) => setChannel(e.target.value)}
            className="w-full rounded-lg border border-gray-200 bg-gray-50/50 px-4 py-2.5 text-sm focus:border-accent focus:ring-1 focus:ring-accent"
          >
            {CHANNEL_OPTIONS.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label
            htmlFor="priority"
            className="mb-2 block text-sm font-bold text-on-surface"
          >
            Priority
          </label>
          <select
            id="priority"
            value={priority}
            onChange={(e) => setPriority(e.target.value)}
            className="w-full rounded-lg border border-gray-200 bg-gray-50/50 px-4 py-2.5 text-sm focus:border-accent focus:ring-1 focus:ring-accent"
          >
            {PRIORITY_OPTIONS.map((opt) => (
              <option key={opt.value} value={opt.value}>
                {opt.label}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="flex flex-col gap-4 sm:flex-row sm:items-center">
        <label className="flex cursor-pointer items-center gap-3 text-sm font-medium text-on-surface">
          <input
            type="checkbox"
            checked={confidential}
            onChange={(e) => setConfidential(e.target.checked)}
            className="h-4 w-4 rounded border-gray-300 text-accent focus:ring-accent"
          />
          Confidential
        </label>
        <label className="flex cursor-pointer items-center gap-3 text-sm font-medium text-on-surface">
          <input
            type="checkbox"
            checked={anonymous}
            onChange={(e) => setAnonymous(e.target.checked)}
            className="h-4 w-4 rounded border-gray-300 text-accent focus:ring-accent"
          />
          Anonymous requester
        </label>
      </div>

      <div className="flex flex-wrap items-center gap-3 border-t border-gray-100 pt-6">
        <button
          type="submit"
          disabled={submitting || !tenantConfigured}
          className="inline-flex items-center justify-center gap-2 rounded-lg bg-gray-900 px-5 py-2.5 text-sm font-bold text-white shadow-sm transition-opacity hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-45"
        >
          {submitting ? (
            <>
              <span className="material-symbols-outlined animate-spin text-[20px]">
                progress_activity
              </span>
              Submitting…
            </>
          ) : (
            <>
              <span className="material-symbols-outlined text-[20px]">save</span>
              Create service request
            </>
          )}
        </button>
        <button
          type="button"
          onClick={() => router.push("/dashboard/service-requests")}
          className="rounded-lg border border-gray-200 px-4 py-2.5 text-sm font-semibold text-gray-600 hover:bg-gray-50"
        >
          Cancel
        </button>
      </div>
    </form>
  );
}
