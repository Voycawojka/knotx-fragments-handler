/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.engine.api;


import io.knotx.fragment.Fragment;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import java.util.Optional;

@DataObject
public class FragmentEvent {

  private final Fragment fragment;
  private KnotFlow flow;
  private final EventLog log;
  private Status status = Status.UNPROCESSED;
  private String errorMessage;

  public FragmentEvent(Fragment fragment, KnotFlow flow) {
    this.fragment = fragment;
    this.flow = flow;
    this.log = new EventLog();
  }

  public FragmentEvent(FragmentEvent event) {
    this.fragment = event.fragment;
    this.log = event.log;
    this.status = event.status;
    this.errorMessage = event.errorMessage;
    this.flow = event.getFlow().isPresent() ? event.getFlow().get() : null;
  }

  public FragmentEvent(JsonObject json) {
    this.fragment = new Fragment(json.getJsonObject("fragment"));
    this.flow = new KnotFlow(json.getJsonObject("flow"));
    this.log = new EventLog(json.getJsonObject("log"));
    this.status = Status.valueOf(json.getString("status"));
    this.errorMessage = json.getString("errorMessage");
  }

  @GenIgnore
  public Optional<KnotFlow> getFlow() {
    return Optional.ofNullable(flow);
  }

  public FragmentEvent setFlow(KnotFlow flow) {
    this.flow = flow;
    return this;
  }

  public FragmentEvent log(EventLogEntry logEntry) {
    log.append(logEntry);
    return this;
  }

  public Fragment getFragment() {
    return fragment;
  }

  public JsonObject getLog() {
    return log.toJson();
  }

  public Status getStatus() {
    return status;
  }

  public FragmentEvent setStatus(Status status) {
    this.status = status;
    return this;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public JsonObject toJson() {
    return new JsonObject()
        .put("fragment", fragment.toJson())
        .put("flow", flow.toJson())
        .put("log", log.toJson())
        .put("status", status)
        .put("errorMessage", errorMessage);
  }

  public enum Status {
    UNPROCESSED, SUCCESS, FAILURE
  }

}
