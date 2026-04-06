import { auth } from "@/auth";
import { randomUUID } from "crypto";
import { NextResponse } from "next/server";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:9080";

export async function POST(req: Request) {
  const session = await auth();
  if (!session?.accessToken) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const tenantId = process.env.TENANT_ID;
  const serviceId = process.env.DEFAULT_SERVICE_ID;
  if (!tenantId || !serviceId) {
    return NextResponse.json(
      {
        error: "Set TENANT_ID and DEFAULT_SERVICE_ID in the server environment (.env).",
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

  const description =
    typeof body.description === "string" ? body.description.trim() : "";
  if (!description) {
    return NextResponse.json({ error: "Description is required" }, { status: 400 });
  }

  const channel =
    typeof body.channel === "string" && body.channel.length > 0
      ? body.channel
      : "WEB";
  const priority =
    typeof body.priority === "string" && body.priority.length > 0
      ? body.priority
      : "NORMAL";
  const confidential = Boolean(body.confidential);
  const anonymous = Boolean(body.anonymous);

  const protocol = `AC-${randomUUID().replace(/-/g, "").slice(0, 12).toUpperCase()}`;

  const payload = {
    tenantId,
    protocol,
    serviceId,
    channel,
    priority,
    description,
    confidential,
    anonymous,
    personId: null,
    addressId: null,
    dynamicFields: null,
    latitude: null,
    longitude: null,
  };

  const res = await fetch(`${API_URL}/api/v1/service-requests`, {
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
      { error: "Failed to create service request", detail },
      { status: res.status },
    );
  }

  const data = (await res.json()) as unknown;
  return NextResponse.json(data, { status: 201 });
}
