/**
 * @fileoverview
 * @enhanceable
 * @public
 */
// GENERATED CODE -- DO NOT EDIT!

goog.provide('proto.common.GPSAddress');

goog.require('jspb.Message');


/**
 * Generated by JsPbCodeGenerator.
 * @param {Array=} opt_data Optional initial data array, typically from a
 * server response, or constructed directly in Javascript. The array is used
 * in place and becomes part of the constructed object. It is not cloned.
 * If no data is provided, the constructed object will be empty, but still
 * valid.
 * @extends {jspb.Message}
 * @constructor
 */
proto.common.GPSAddress = function(opt_data) {
  jspb.Message.initialize(this, opt_data, 0, -1, null, null);
};
goog.inherits(proto.common.GPSAddress, jspb.Message);
if (goog.DEBUG && !COMPILED) {
  proto.common.GPSAddress.displayName = 'proto.common.GPSAddress';
}


if (jspb.Message.GENERATE_TO_OBJECT) {
/**
 * Creates an object representation of this proto suitable for use in Soy templates.
 * Field names that are reserved in JavaScript and will be renamed to pb_name.
 * To access a reserved field use, foo.pb_<name>, eg, foo.pb_default.
 * For the list of reserved names please see:
 *     com.google.apps.jspb.JsClassTemplate.JS_RESERVED_WORDS.
 * @param {boolean=} opt_includeInstance Whether to include the JSPB instance
 *     for transitional soy proto support: http://goto/soy-param-migration
 * @return {!Object}
 */
proto.common.GPSAddress.prototype.toObject = function(opt_includeInstance) {
  return proto.common.GPSAddress.toObject(opt_includeInstance, this);
};


/**
 * Static version of the {@see toObject} method.
 * @param {boolean|undefined} includeInstance Whether to include the JSPB
 *     instance for transitional soy proto support:
 *     http://goto/soy-param-migration
 * @param {!proto.common.GPSAddress} msg The msg instance to transform.
 * @return {!Object}
 */
proto.common.GPSAddress.toObject = function(includeInstance, msg) {
  var f, obj = {
    address: jspb.Message.getField(msg, 1),
    unit: jspb.Message.getField(msg, 2),
    latitude: jspb.Message.getField(msg, 3),
    longitude: jspb.Message.getField(msg, 4)
  };

  if (includeInstance) {
    obj.$jspbMessageInstance = msg
  }
  return obj;
};
}


/**
 * Creates a deep clone of this proto. No data is shared with the original.
 * @return {!proto.common.GPSAddress} The clone.
 */
proto.common.GPSAddress.prototype.cloneMessage = function() {
  return /** @type {!proto.common.GPSAddress} */ (jspb.Message.cloneMessage(this));
};


/**
 * optional string address = 1;
 * @return {string?}
 */
proto.common.GPSAddress.prototype.getAddress = function() {
  return /** @type {string?} */ (jspb.Message.getField(this, 1));
};


/** @param {string?|undefined} value  */
proto.common.GPSAddress.prototype.setAddress = function(value) {
  jspb.Message.setField(this, 1, value);
};


proto.common.GPSAddress.prototype.clearAddress = function() {
  jspb.Message.setField(this, 1, undefined);
};


/**
 * optional string unit = 2;
 * @return {string?}
 */
proto.common.GPSAddress.prototype.getUnit = function() {
  return /** @type {string?} */ (jspb.Message.getField(this, 2));
};


/** @param {string?|undefined} value  */
proto.common.GPSAddress.prototype.setUnit = function(value) {
  jspb.Message.setField(this, 2, value);
};


proto.common.GPSAddress.prototype.clearUnit = function() {
  jspb.Message.setField(this, 2, undefined);
};


/**
 * optional double latitude = 3;
 * @return {number?}
 */
proto.common.GPSAddress.prototype.getLatitude = function() {
  return /** @type {number?} */ (jspb.Message.getField(this, 3));
};


/** @param {number?|undefined} value  */
proto.common.GPSAddress.prototype.setLatitude = function(value) {
  jspb.Message.setField(this, 3, value);
};


proto.common.GPSAddress.prototype.clearLatitude = function() {
  jspb.Message.setField(this, 3, undefined);
};


/**
 * optional double longitude = 4;
 * @return {number?}
 */
proto.common.GPSAddress.prototype.getLongitude = function() {
  return /** @type {number?} */ (jspb.Message.getField(this, 4));
};


/** @param {number?|undefined} value  */
proto.common.GPSAddress.prototype.setLongitude = function(value) {
  jspb.Message.setField(this, 4, value);
};


proto.common.GPSAddress.prototype.clearLongitude = function() {
  jspb.Message.setField(this, 4, undefined);
};

