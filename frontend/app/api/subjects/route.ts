import { auth } from "@/auth";
import { NextResponse } from "next/server";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:9080";

export async function POST(req: Request) {
  const session = await auth();
  if (!session?.accessToken) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const tenantId = process.env.TENANT_ID;
  if (!tenantId) {
    return NextResponse.json(
      {
        error: "Set TENANT_ID in the server environment (.env).",
      },
      { status: 503 },
    );
  }

  let body: Record<string, unknown>;
  try {
    body = await req.json();
  } catch {
    return NextResponse.json({ error: "Invalid JSON" }, { status: 400 });
  }

  const name = typeof body.name === "string" ? body.name.trim() : "";
  if (!name) {
    return NextResponse.json({ error: "Name is required" }, { status: 400 });
  }

  const departmentIdRaw = body.departmentId;
  const departmentId =
    typeof departmentIdRaw === "string" && departmentIdRaw.trim().length > 0
      ? departmentIdRaw.trim()
      : null;

  const visibleWeb = Boolean(body.visibleWeb);
  const visibleApp = Boolean(body.visibleApp);

  const payload: Record<string, unknown> = {
    tenantId,
    name,
    visibleWeb,
    visibleApp,
  };
  if (departmentId) {
    payload.departmentId = departmentId;
  }

  const res = await fetch(`${API_URL}/api/v1/subjects`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${session.accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const detail = await res.text();
    return NextResponse.json(
      { error: "Failed to create subject", detail },
      { status: res.status },
    );
  }

  const data = (await res.json()) as unknown;
  return NextResponse.json(data, { status: 201 });
}
