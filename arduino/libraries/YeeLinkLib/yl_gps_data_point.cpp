#include "yl_gps_data_point.h"

yl_gps_data_point::yl_gps_data_point()
: speed_(0)
, offset_(true)
{
	memset(&loc_, 0, sizeof(loc_));
}
yl_gps_data_point::yl_gps_data_point(const location &loc, float speed, bool offset, const String &key)
: yl_data_point(key)
, speed_(speed)
, offset_(offset)
, loc_(loc)
{}

void yl_gps_data_point::set_location(const location &loc)
{
	loc_ = loc;
}

const location& yl_gps_data_point::get_location() const
{
	return loc_;
}

void yl_gps_data_point::set_speed(float speed)
{
	speed_ = speed;
}

float yl_gps_data_point::get_speed() const
{
	return speed_;
}

void yl_gps_data_point::set_offset(bool offset)
{
	offset_ = offset;
}

bool yl_gps_data_point::get_offset() const
{
	return offset_;
}

String yl_gps_data_point::value_to_string() const
{
	String value("{\"lat\":");
	value += ftoa(loc_.lat, 2);
	value += ",\"lng\":";
	value += ftoa(loc_.lng, 2);
	value += ",\"speed\":";
	value += ftoa(speed_, 2);
	value += ",\"offset\":";
	value += (offset_ ? "\"yes\"}" : "\"no\"}");
	return value;
}

bool yl_gps_data_point::value_from_string(const String &str)
{
	if (str.length() == 0)
	{
		return false;
	}

	String value = sub_string(str, 0, "\"lat\":", ",");
	loc_.lat = atof(&value[0]);
	int start_index = str.indexOf(",") + 1;
	value = sub_string(str, start_index, "\"lng\":", ",");
	loc_.lng = atof(&value[0]);
	start_index = str.indexOf(",", start_index) + 1;
	value = sub_string(str, start_index, "\"speed\":", ",");
	if (value.length() == 0)
	{
		value = sub_string(str, start_index, "\"speed\":", "}");
		speed_ = atof(&value[0]);
		return true;
	}
	speed_ = atof(&value[0]);
	start_index = str.indexOf(",", start_index) + 1;
	value = sub_string(str, start_index, "\"offset\":\"", "\"}");
	offset_ = (value == "yes" ? true : false);
	return true;
}

